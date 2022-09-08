package com.uniqueapps.navixbrowser.object;

import java.io.Serializable;
import java.util.Date;

public class DownloadObject implements Serializable {

	private static final long serialVersionUID = 4776520847715022370L;

	public static enum DownloadState {
		DOWNLOADING, FINISHED
	}
	
	public static enum DownloadAction {
		PAUSE, RESUME, CANCEL
	}

	public int id;
	public String name;
	public String url;
	public Date date;
	public DownloadState downloadState;
	
	public DownloadObject(int id, String name, String url, Date date, DownloadState downloadState) {
		this.id = id;
		this.name = name;
		this.url = url;
		this.date = date;
		this.downloadState = downloadState;
	}
}
