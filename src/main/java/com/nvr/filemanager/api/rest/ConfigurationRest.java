package com.nvr.filemanager.api.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.nvr.filemanager.configuration.ConfigFileManager;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class ConfigurationRest {

	@Autowired
	private ConfigFileManager configFileManagerg;

	/**
	 * Configuratione REST
	 *
	 * @return
	 */
	@RequestMapping(path = "/configuration", method = RequestMethod.GET, name = "configuration")
	public @ResponseBody ConfigFileManager configuration() {
		return this.configFileManagerg;
	}

}