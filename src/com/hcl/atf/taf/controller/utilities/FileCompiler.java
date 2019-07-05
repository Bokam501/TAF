package com.hcl.atf.taf.controller.utilities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FileCompiler {
	private static final Log log = LogFactory.getLog(FileCompiler.class);
	
	public void findFilesList(File source) {

		if (source.isDirectory()) {
			for (File nestedFile : source.listFiles()) {
				if (nestedFile.isDirectory()) {
					findFilesList(nestedFile);
				} else {
					if (nestedFile.toString().endsWith(".java")) {
					} else {
						continue;
					}
				}
			}
		}

	}

	public String findLibrariesList(File libs, String libPath) {


		if (libs.isDirectory()) {
			for (File nestedFile : libs.listFiles()) {
				if (nestedFile.isDirectory()) {
					findLibrariesList(nestedFile, libPath);
				} else {
					if (nestedFile.toString().endsWith(".jar")
							|| nestedFile.toString().endsWith(".java")) {
						if (!libPath.isEmpty()) {
							libPath = libPath + ";" + nestedFile.toString();
						} else {
							libPath = nestedFile.toString();
						}

					} else {
						continue;
					}
				}
			}
		}
		return libPath;
	}

	public void compile(File fileToCompile, String outputPath) {
		try {
			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
			StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);

			
			String fileObectPath = fileToCompile.toString();
			List<String> libPathOption = new ArrayList<String>();

			File libPath = new File(outputPath+"\\lib");

			String binPath = outputPath+"\\bin";

			String sourcePath = outputPath+"\\src";

			String libFilesPath = findLibrariesList(libPath, "");			

			log.info("The libraries path : libFilesPath :  "+ libFilesPath);

			libPathOption.addAll(Arrays.asList("-classpath", libFilesPath));
			libPathOption.addAll(Arrays.asList("-d", binPath));
			libPathOption.addAll(Arrays.asList("-sourcepath", sourcePath));

			log.info("Compilation starts: " + fileObectPath);
			Iterable compilationUnits1 = fileManager.getJavaFileObjects(fileObectPath);
			CompilationTask task = compiler.getTask(null, fileManager, null,libPathOption, null, compilationUnits1);

			// Perform the compilation task.
			task.call();
			fileManager.close();

		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

}
