package com.nvr.filemanager.model.request;

import java.io.Serializable;

import com.nvr.filemanager.model.Mode;

public abstract class AbstractParams implements Serializable {

	private static final long serialVersionUID = -446741936675338791L;

	protected Mode action;

	/**
	 * @return the action
	 */
	protected abstract Mode getAction();

	/**
	 * @param action the action to set
	 */
	public void setAction(Mode action) {
		this.action = action;
	}
}
