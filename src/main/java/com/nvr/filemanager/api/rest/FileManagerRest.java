package com.nvr.filemanager.api.rest;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nvr.filemanager.constants.CommonConstants;
import com.nvr.filemanager.model.request.ChangePermission;
import com.nvr.filemanager.model.request.Compress;
import com.nvr.filemanager.model.request.Copy;
import com.nvr.filemanager.model.request.CreateFolder;
import com.nvr.filemanager.model.request.Edit;
import com.nvr.filemanager.model.request.Extract;
import com.nvr.filemanager.model.request.GetContent;
import com.nvr.filemanager.model.request.GetList;
import com.nvr.filemanager.model.request.Move;
import com.nvr.filemanager.model.request.Remove;
import com.nvr.filemanager.model.request.Rename;
import com.nvr.filemanager.model.response.Item;
import com.nvr.filemanager.model.response.ResponseModel;
import com.nvr.filemanager.model.response.ResponseStatus;
import com.nvr.filemanager.model.response.SuccessfulResponse;
import com.nvr.filemanager.service.FileManager;

/**
 * @see https://github.com/joni2back/angular-filemanager/blob/master/API.md
 */
@RestController
@RequestMapping(path = "action", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class FileManagerRest {

	@Autowired
	private FileManager fileManager;

	/**
	 * Listing (URL: fileManagerConfig.listUrl, Method: POST)
	 *
	 * @return
	 */
	@PostMapping("/list")
	public @ResponseBody ResponseModel<List<Item>> list(@RequestBody GetList params) {
		return new ResponseModel<>(fileManager.list(params.getPath()));
	}

	/**
	 * Rename (URL: fileManagerConfig.renameUrl, Method: POST)
	 *
	 * @return
	 */
	@PostMapping("/rename")
	public @ResponseBody ResponseModel<ResponseStatus> rename(@RequestBody Rename params) {
		fileManager.rename(params.getItem(), params.getNewItemPath());
		return new SuccessfulResponse();
	}

	/**
	 * Move (URL: fileManagerConfig.moveUrl, Method: POST)
	 *
	 * @return
	 */
	@PostMapping("/move")
	public @ResponseBody ResponseModel<ResponseStatus> move(@RequestBody Move params) {
		fileManager.move(params.getItems(), params.getNewPath());
		return new SuccessfulResponse();
	}

	/**
	 * Copy (URL: fileManagerConfig.copyUrl, Method: POST)
	 *
	 * @return
	 */
	@PostMapping("/copy")
	public @ResponseBody ResponseModel<ResponseStatus> copy(@RequestBody Copy params) {
		fileManager.copy(params.getItems(), params.getNewPath(), params.getSingleFilename());
		return new SuccessfulResponse();
	}

	/**
	 * Remove (URL: fileManagerConfig.removeUrl, Method: POST)
	 *
	 * @return
	 */
	@PostMapping("/remove")
	public @ResponseBody ResponseModel<ResponseStatus> remove(@RequestBody Remove params) {
		fileManager.remove(params.getItems());
		return new SuccessfulResponse();
	}

	/**
	 * Edit file (URL: fileManagerConfig.editUrl, Method: POST)
	 *
	 * @return
	 */
	@PostMapping("/edit")
	public @ResponseBody ResponseModel<ResponseStatus> edit(@RequestBody Edit params) {
		return new ResponseModel<>(new ResponseStatus(fileManager.edit(params.getItem(), params.getContent())));
	}

	/**
	 * Get content of a file (URL: fileManagerConfig.getContentUrl, Method:
	 * POST)
	 *
	 * @return
	 */
	@PostMapping("/get-content")
	public @ResponseBody ResponseModel<String> getContent(@RequestBody GetContent params) {
		return new ResponseModel<>(fileManager.getContent(params.getItem()));
	}

	/**
	 * Create folder (URL: fileManagerConfig.createFolderUrl, Method: POST)
	 *
	 * @return
	 */
	@PostMapping("/create-folder")
	public @ResponseBody ResponseModel<ResponseStatus> createFolder(@RequestBody CreateFolder params) {
		fileManager.createFolder(params.getNewPath());
		return new SuccessfulResponse();
	}

	/**
	 * Change Permission (URL: fileManagerConfig.permissionUrl, Method: POST)
	 *
	 * @return
	 */
	@PostMapping("/permission")
	public @ResponseBody ResponseModel<ResponseStatus> changePermission(@RequestBody ChangePermission params) {
		fileManager.changePermission(params.getItems(), params.getPerms(), params.getPermsCode(), params.isRecursive());
		return new SuccessfulResponse();
	}

	/**
	 * Compress file (URL: fileManagerConfig.compressUrl, Method: POST)
	 *
	 * @return
	 */
	@PostMapping("/compress")
	public @ResponseBody ResponseModel<ResponseStatus> compress(@RequestBody Compress params) {
		fileManager.compress(params.getItems(), params.getDestination(), params.getCompressedFilename());
		return new SuccessfulResponse();
	}

	/**
	 * Extract file (URL: fileManagerConfig.extractUrl, Method: POST)
	 *
	 * @return
	 */
	@PostMapping("/extract")
	public @ResponseBody ResponseModel<ResponseStatus> extract(@RequestBody Extract params) {
		fileManager.extract(params.getItem(), params.getFolderName(), params.getDestination());
		return new SuccessfulResponse();
	}

	/**
	 * Upload file (URL: fileManagerConfig.uploadUrl, Method: POST,
	 * Content-Type: multipart/form-data)
	 *
	 * @return
	 *
	 * @return
	 */
	@PostMapping(path = "/upload",consumes= MediaType.MULTIPART_FORM_DATA_VALUE)
	public @ResponseBody ResponseModel<ResponseStatus> upload(@RequestParam(name="file") List<MultipartFile> files,@RequestParam(name = "destination", required = true) String destination) {
		files.stream().forEach((m) -> fileManager.store(m, destination));
		return new SuccessfulResponse();
	}

	/**
	 * Download / Preview file (URL: fileManagerConfig.downloadMultipleUrl,
	 * Method: GET)
	 *
	 * @return
	 */
	@GetMapping(path = "/download",consumes = MediaType.ALL_VALUE,produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public FileSystemResource download(HttpServletResponse response, @RequestParam(name = "path", required = true) String path) {
	    return new FileSystemResource(fileManager.getFile(path));
	}

	/**
	 * Download multiples files in ZIP/TAR (URL:
	 * fileManagerConfig.downloadFileUrl, Method: GET)
	 *
	 * @return
	 * @throws IOException 
	 */
	@GetMapping(path = "/download-multiple",consumes = MediaType.ALL_VALUE,produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public void downloadMultiple(HttpServletResponse response,@RequestParam("items") List<String> paths,@RequestParam("toFilename") String toFilename) throws IOException{
		response.setContentType(CommonConstants.APPLICATION_ZIP);
        response.setHeader(CommonConstants.CONTENT_DISPOSITION, CommonConstants.downloadFileName(toFilename));
		try(ServletOutputStream os = response.getOutputStream()){
			fileManager.files2Zip(paths, os);
			os.flush();
		}
	}

}
