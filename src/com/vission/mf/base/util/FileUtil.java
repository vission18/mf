package com.vission.mf.base.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.vission.mf.base.exception.ServiceException;

public class FileUtil {
	private static final Logger logger = Logger.getLogger(FileUtil.class);

	private final static int BUFFER = 1024;

	/**
	 * 功 能: 移动文件(只能移动文件) 参 数: strSourceFileName:指定的文件全路径名 strDestDir: 移动到指定的文件夹
	 * 返回值: 如果成功true;否则false
	 * 
	 * @param strSourceFileName
	 * @param strDestDir
	 * @return
	 */
	public static boolean copyTo(String strSourceFileName, String strDestDir) {
		File fileSource = new File(strSourceFileName);
		File fileDest = new File(strDestDir);

		// 如果源文件不存或源文件是文件夹
		if (!fileSource.exists() || !fileSource.isFile()) {
			logger.debug("源文件[" + strSourceFileName + "],不存在或是文件夹!");
			return false;
		}

		// 如果目标文件夹不存在
		if (!fileDest.isDirectory() || !fileDest.exists()) {
			if (!fileDest.mkdirs()) {
				logger.debug("目录文件夹不存，在创建目标文件夹时失败!");
				return false;
			}
		}

		try {
			String strAbsFilename = strDestDir + File.separator
					+ fileSource.getName();

			FileInputStream fileInput = new FileInputStream(strSourceFileName);
			FileOutputStream fileOutput = new FileOutputStream(strAbsFilename);

			logger.debug("开始拷贝文件");

			int count = -1;

			long nWriteSize = 0;
			long nFileSize = fileSource.length();

			byte[] data = new byte[BUFFER];

			while (-1 != (count = fileInput.read(data, 0, BUFFER))) {

				fileOutput.write(data, 0, count);

				nWriteSize += count;

				long size = (nWriteSize * 100) / nFileSize;
				long t = nWriteSize;

				String msg = null;

				if (size <= 100 && size >= 0) {
					msg = "\r拷贝文件进度:   " + size + "%   \t" + "\t   已拷贝:   " + t;
					logger.debug(msg);
				} else if (size > 100) {
					msg = "\r拷贝文件进度:   " + 100 + "%   \t" + "\t   已拷贝:   " + t;
					logger.debug(msg);
				}

			}

			fileInput.close();
			fileOutput.close();

			logger.debug("拷贝文件成功!");
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 功 能: 删除指定的文件 参 数: 指定绝对路径的文件名 strFileName 返回值: 如果删除成功true否则false;
	 * 
	 * @param strFileName
	 * @return
	 */
	public static boolean delete(String strFileName) {
		File fileDelete = new File(strFileName);

		if (!fileDelete.exists() || !fileDelete.isFile()) {
			logger.debug(strFileName + "不存在!");
			return false;
		}

		return fileDelete.delete();
	}

	/**
	 * 功 能: 移动文件(只能移动文件) 参 数: strSourceFileName: 是指定的文件全路径名 strDestDir:
	 * 移动到指定的文件夹中 返回值: 如果成功true; 否则false
	 * 
	 * @param strSourceFileName
	 * @param strDestDir
	 * @return
	 */
	public static boolean moveFile(String strSourceFileName, String strDestDir) {
		if (copyTo(strSourceFileName, strDestDir))
			return delete(strSourceFileName);
		else
			return false;
	}

	/**
	 * 功 能: 创建文件夹 参 数: strDir 要创建的文件夹名称 返回值: 如果成功true;否则false
	 * 
	 * @param strDir
	 * @return
	 */
	public static boolean makeDir(String strDir) {
		File fileNew = new File(strDir);

		if (!fileNew.exists()) {
			return fileNew.mkdirs();
		} else {
			return true;
		}
	}

	/**
	 * 功 能: 删除文件夹 参 数: strDir 要删除的文件夹名称 返回值: 如果成功true;否则false
	 * 
	 * @param strDir
	 * @return
	 */
	public static boolean removeDir(String strDir) {
		File rmDir = new File(strDir);
		if (rmDir.isDirectory() && rmDir.exists()) {
			String[] fileList = rmDir.list();

			for (int i = 0; i < fileList.length; i++) {
				String subFile = strDir + File.separator + fileList[i];
				File tmp = new File(subFile);
				if (tmp.isFile())
					tmp.delete();
				else if (tmp.isDirectory())
					removeDir(subFile);
			}
			rmDir.delete();
		} else {
			return false;
		}
		return true;
	}

	/**
	 * 将文件转成输出流
	 * 
	 * @param fileName
	 * @return
	 */
	public static ByteArrayOutputStream file2OutStream(String fileName)
			throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		File file = new File(fileName);
		InputStream in = new FileInputStream(file);
		byte[] buffer = new byte[1024*1024];
		int reader = 0;
		while ((reader = in.read(buffer)) != -1) {
			out.write(buffer, 0, reader);
		}
		in.close();
		out.close();
		return out;
	}

	public static Map<String, String> iconMap = new HashMap<String, String>();
	static {
		iconMap.put("BMP", "ico_file_bmp.gif");
		iconMap.put("RAR", "ico_file_rar.gif");
		iconMap.put("ZIP", "ico_file_zip.gif");
		iconMap.put("TXT", "ico_file_txt.gif");
		iconMap.put("DOC", "ico_file_doc.gif");
		iconMap.put("XLS", "ico_file_xls.gif");
		iconMap.put("DIR", "ico_folder.gif");
		iconMap.put("EXE", "ico_file_exe.gif");
		iconMap.put("GIF", "ico_file_gif.gif");
		iconMap.put("HTM", "ico_file_htm.gif");
		iconMap.put("JPG", "ico_file_jpg.gif");
		iconMap.put("OTH", "ico_file.gif");
		iconMap.put("DOCX", "ico_file_doc.gif");
		iconMap.put("XLSX", "ico_file_xls.gif");
	}

	public static void download(String fileName, String realFileName,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		InputStream input = FileUtils.openInputStream(new File(realFileName));
		byte[] data = IOUtils.toByteArray(input);
		if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {
			fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
		} else if (request.getHeader("User-Agent").toUpperCase()
				.indexOf("MSIE") > 0) {
			fileName = URLEncoder.encode(fileName, "UTF-8");
		}
		response.reset();
		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ fileName + "\"");
		response.addHeader("Content-Length", "" + data.length);
		response.setContentType("application/octet-stream; charset=UTF-8");
		IOUtils.write(data, response.getOutputStream());
		IOUtils.closeQuietly(input);
	}

	public static void download(String fileName, byte[] dataByte,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		byte[] data = dataByte;
		if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {
			fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
		} else if (request.getHeader("User-Agent").toUpperCase()
				.indexOf("MSIE") > 0) {
			fileName = URLEncoder.encode(fileName, "UTF-8");
		}
		response.reset();
		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ fileName + "\"");
		response.addHeader("Content-Length", "" + data.length);
		response.setContentType("application/octet-stream; charset=UTF-8");
		IOUtils.write(data, response.getOutputStream());
	}
	
	/**
	 * 将流转换成list,并生成零时文件
	 * @param fTmp 
	 * @param file
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Object> readFile(InputStream is, File fTmp) throws ServiceException {
		ByteArrayOutputStream bos = null;
		ByteArrayInputStream bis = null;
		ObjectInputStream ois = null;
		FileOutputStream Out = null; 
		try{
			bos = new ByteArrayOutputStream();
			int len;
			byte[] b = new byte[1024];
			Out = new FileOutputStream(fTmp);
			while ((len = is.read(b)) != -1) {
				Out.write(b,0,len);
				bos.write(b, 0, len);
			}
			Out.close();
			byte[] bytes = bos.toByteArray();
			
			bis = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(bis);
			List<Object> list = (List<Object>) ois.readObject();
			return list;
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		} finally {
			try {
				if(bos!=null){
					bos.close();
					bos = null;	
				}
				if(is!=null){
					is.close();
					is = null;
				}
				if(ois!=null){
					ois.close();
					ois = null;
				}
				if(bis!=null){
					bis.close();
					bis = null;	
				}
				if(Out!=null){
					Out.close();
					Out = null;	
				}	
			} catch (IOException e) {
				throw new ServiceException(e.getMessage());
			}
		}

	}
	
	/**
	 * 将流转换成list
	 * @param file
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Object> readFile(InputStream is) throws ServiceException {
		ByteArrayOutputStream bos = null;
		ByteArrayInputStream bis = null;
		ObjectInputStream ois = null;
		try{
			bos = new ByteArrayOutputStream();
			int len;
			byte[] b = new byte[1024];
			while ((len = is.read(b)) != -1) {
				bos.write(b, 0, len);
			}
			byte[] bytes = bos.toByteArray();
			
			bis = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(bis);
			List<Object> list = (List<Object>) ois.readObject();
			return list;
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		} finally {
			try {
				if(bos!=null){
					bos.close();
					bos = null;	
				}
				if(is!=null){
					is.close();
					is = null;
				}
				if(ois!=null){
					ois.close();
					ois = null;
				}
				if(bis!=null){
					bis.close();
					bis = null;	
				}
			} catch (IOException e) {
				throw new ServiceException(e.getMessage());
			}
		}

	}
	
	public static void download(String fileName, List<Object> o,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		ServletOutputStream out = null;
		response.reset();
		if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {
			fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
		} else if (request.getHeader("User-Agent").toUpperCase()
				.indexOf("MSIE") > 0) {
			fileName = URLEncoder.encode(fileName, "UTF-8");
		}
		response.reset();
		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ fileName + "\"");
		response.setContentType("application/octet-stream; charset=UTF-8");
		out = response.getOutputStream();
		ObjectOutputStream oos = null;
		oos = new ObjectOutputStream(out);
		oos.writeObject(o);
		out.flush();
		out.close();
	}
	
	public static HSSFRow createRow(HSSFSheet sheet, int index) {
		return sheet.createRow(index);
	}

	public static HSSFCell createCell(HSSFRow row, int index) {
		return row.createCell(index);
	}

	public static void download(String fileName, HSSFWorkbook workbook,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		OutputStream Out = response.getOutputStream();
		response.reset();
		response.setContentType("application/vnd.ms-excel");
		fileName = fileName + ".xls";
		if (request.getHeader("user-agent").indexOf("MSIE") != -1) {
			fileName = URLEncoder.encode(fileName, "utf-8");
		} else {
			fileName = new String(fileName.getBytes("utf-8"), "iso-8859-1");
		}
		response.setHeader("Content-Disposition", "attachment; filename="
				+ fileName);
		workbook.write(Out);
		Out.close();
	}

	
	
	public static void copyFile(String infileName, String outfileName, boolean makeDir) throws FileNotFoundException, IOException {
		File infile = new File(infileName);
		File outfile = new File(outfileName);
		if(makeDir){
			if(!outfile.getParentFile().exists()){
				outfile.getParentFile().mkdirs();
			}
		}
		copyFile(infile, outfile);
	}
	
	public static void copyFile(File infile, File outfile) throws FileNotFoundException, IOException {
		FileInputStream fin = null;
		FileOutputStream fout = null;
		FileChannel fcin = null;
		FileChannel fcout = null;
		try{
			fin = new FileInputStream(infile);
			fout = new FileOutputStream(outfile);
			fcin = fin.getChannel();
			fcout = fout.getChannel();
			ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
			while (true) {
				buffer.clear();
				int r = fcin.read(buffer);
				if (r == -1) {
					break;
				}
				buffer.flip();
				fcout.write(buffer);
			}
		}catch(FileNotFoundException fnfe){
			throw new FileNotFoundException(fnfe.getMessage());
		}catch(IOException ioe){
			throw new FileNotFoundException(ioe.getMessage());
		}finally{
			if(fcout != null) fcout.close();
			if(fcin != null) fcin.close();
			if(fout != null) fout.close();
			if(fin != null) fin.close();
			outfile = null;
			infile = null;
		}
		
	}
}