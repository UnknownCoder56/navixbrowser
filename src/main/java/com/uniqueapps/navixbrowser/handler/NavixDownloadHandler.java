package com.uniqueapps.navixbrowser.handler;

import java.io.File;

import org.cef.browser.CefBrowser;
import org.cef.callback.CefBeforeDownloadCallback;
import org.cef.callback.CefDownloadItem;
import org.cef.callback.CefDownloadItemCallback;
import org.cef.handler.CefDownloadHandlerAdapter;

import com.uniqueapps.navixbrowser.Main;
import com.uniqueapps.navixbrowser.component.BrowserWindow;
import com.uniqueapps.navixbrowser.component.DownloadObjectPanel;
import com.uniqueapps.navixbrowser.component.DownloadsPanel;
import com.uniqueapps.navixbrowser.object.DownloadObject;
import com.uniqueapps.navixbrowser.object.DownloadObject.DownloadState;

public class NavixDownloadHandler extends CefDownloadHandlerAdapter {

	BrowserWindow browserWindow;
	
	public NavixDownloadHandler(BrowserWindow browserWindow) {
		this.browserWindow = browserWindow;
	}
	
	@Override
	public boolean onBeforeDownload(CefBrowser cefBrowser, CefDownloadItem cefDownloadItem, String suggestedFileName,
			CefBeforeDownloadCallback cefBeforeDownloadCallback) {
		super.onBeforeDownload(cefBrowser, cefDownloadItem, suggestedFileName, cefBeforeDownloadCallback);
		var downloadObject = new DownloadObject(cefDownloadItem.getId(), suggestedFileName, cefDownloadItem.getURL(),
				cefDownloadItem.getStartTime(), DownloadState.DOWNLOADING);
		Main.downloads.add(downloadObject);
		Main.downloadPanels.add(new DownloadObjectPanel(browserWindow, downloadObject));
		if (browserWindow.tabbedPane.downloadsTabOpen) {
			(
					(DownloadsPanel) browserWindow.tabbedPane.getComponentAt(
							browserWindow.tabbedPane.indexOfTab("Downloads")
					)
			).refreshDownloadPanels();
		}
		Main.refreshDownloads();
		cefBeforeDownloadCallback.Continue(
				new File(new File(System.getProperty("user.home", "Downloads")), suggestedFileName).getAbsolutePath(),
				true);
		return true;
	}

	@Override
	public void onDownloadUpdated(CefBrowser cefBrowser, CefDownloadItem cefDownloadItem,
			CefDownloadItemCallback cefDownloadItemCallback) {
		super.onDownloadUpdated(cefBrowser, cefDownloadItem, cefDownloadItemCallback);
		Main.downloadPanels.stream().filter(downloadPanel -> downloadPanel.downloadObject.id == cefDownloadItem.getId() && downloadPanel.downloadObject.downloadState != DownloadState.FINISHED)
				.findFirst().ifPresent(downloadPanel -> {
					downloadPanel.progressBar.setValue(cefDownloadItem.getPercentComplete());
					downloadPanel.downloadSpeed.setText(getDataSize(cefDownloadItem.getCurrentSpeed()) + "/s");
					String doneBytes = getDataSize(cefDownloadItem.getReceivedBytes());
					String totalBytes = getDataSize(cefDownloadItem.getTotalBytes());
					downloadPanel.partDone.setText(doneBytes + " / " + totalBytes);
				});
		if (!Main.downloadsActionBuffer.isEmpty()) {
			Main.downloads.stream().filter(downloadObject -> downloadObject.id == cefDownloadItem.getId() && downloadObject.downloadState != DownloadState.FINISHED).findFirst()
					.ifPresent(downloadObject -> {
						if (Main.downloadsActionBuffer.containsKey(downloadObject)) {
							var action = Main.downloadsActionBuffer.get(downloadObject);
							switch (action) {
								case PAUSE:
									cefDownloadItemCallback.pause();
									Main.downloadPanels.stream().filter(
										downloadPanel -> downloadPanel.downloadObject.id == cefDownloadItem.getId() && downloadPanel.downloadObject.downloadState != DownloadState.FINISHED)
										.findFirst()
										.ifPresent(downloadPanel -> downloadPanel.callback = cefDownloadItemCallback);
									break;
								case RESUME:
									cefDownloadItemCallback.resume();
									break;
								case CANCEL:
									cefDownloadItemCallback.cancel();
									break;
							}
							Main.downloadsActionBuffer.remove(downloadObject);
						}
					});
		}
		if (cefDownloadItem.getPercentComplete() == 100) {
			Main.downloadPanels.stream()
					.filter(downloadPanel -> downloadPanel.downloadObject.id == cefDownloadItem.getId() && downloadPanel.downloadObject.downloadState != DownloadState.FINISHED).findFirst()
					.ifPresent(downloadPanel -> {
						downloadPanel.downloadObject.downloadState = DownloadState.FINISHED;
						downloadPanel.progressBar.setVisible(false);
						downloadPanel.actions.setVisible(false);
						new Thread(Main::refreshDownloads).start();
					});
		}
	}

	private String getDataSize(long bytes) {
		long KB = bytes / 1024;
		long MB = KB / 1024;
		long GB = MB / 1024;
		if (GB > 1) {
			return Math.round(GB * 100.0) / 100.0 + " GB";
		} else if (MB > 1) {
			return Math.round(MB * 100.0) / 100.0 + " MB";
		} else if (KB > 1) {
			return Math.round(KB * 100.0) / 100.0 + " KB";
		} else {
			return Math.round(bytes * 100.0) / 100.0 + " bytes";
		}
	}
}
