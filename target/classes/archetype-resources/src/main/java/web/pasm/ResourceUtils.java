package ${package}.web.pasm;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ResourceUtils {
	/**
	 * 获取文件夹下的文件
	 *
	 * @param dirPath "sql/mysql"
	 * @param suffix  ".xml"
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> getFiles(String dirPath, String suffix) throws Exception {
		dirPath = formatPath(dirPath);

		URL url = ClassLoader.getSystemResource(dirPath);
		if (url == null) {
			throw new Exception(dirPath);
		}

		URI uri = url.toURI();
		String schema = uri.getScheme();

		switch (schema) {
			case "jar":
				return loadFromJar(uri, suffix);
			case "file":
				return loadFromDir(uri, suffix);
			default:
				throw new NotImplementedException(schema);
		}
	}

	/**
	 * 在jar中读取配置文件
	 *
	 * @param uri    "jar:file:/Users/.../xxx.jar!/sql/mysql"
	 * @param suffix ".xml"
	 * @throws Exception
	 */
	private static Map<String, String> loadFromJar(URI uri, String suffix) throws Exception {
		URI fileUri = getSchemeSpecificPart(uri, "file");

		String[] items = fileUri.toString().split("!");
		if (items.length != 2) {
			throw new Exception();
		}

		Map<String, String> map = new LinkedHashMap<>();
		String jarFilePath = items[0];
		String resDirPath = formatPath(items[1]);

		Enumeration<JarEntry> entries = new JarFile(jarFilePath).entries();
		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			String name = entry.getName();

			if (!name.startsWith(resDirPath + "/")) {
				continue;
			}

			if (entry.isDirectory()) {
				URI childUri = new URI(uri.toString() + "/" + name);
				map.putAll(loadFromJar(childUri, suffix));
				continue;
			}

			if (StringUtils.isEmpty(suffix) || name.endsWith(suffix)) {
				String fileName = name;
				int index = fileName.lastIndexOf("/");
				if (index != -1) {
					fileName = fileName.substring(index + 1);
				}

				InputStream inputStream = ResourceUtils.class.getResourceAsStream("/" + name);
				String content;
				try {
					content = FileUtils.readStream(inputStream);
				} catch (IOException e) {
					Utils.close(inputStream);
					throw e;
				}

				if (map.containsKey(fileName)) {
					map.put(uri.getPath() + "." + fileName, content);
				} else {
					map.put(fileName, content);
				}
			}
		}

		return map;
	}

	/**
	 * 在dir中读取配置文件
	 *
	 * @param uri
	 * @param suffix ".xml"
	 * @return
	 */
	private static Map<String, String> loadFromDir(URI uri, String suffix) throws Exception {
		FilenameFilter filter = null;

		if (!StringUtils.isEmpty(suffix)) {
			filter = (dir, name) -> name.endsWith(suffix) || dir.isDirectory();
		}

		File[] files = new File(uri).listFiles(filter);
		if (files == null) {
			return null;
		}

		Map<String, String> map = new LinkedHashMap<>();
		for (File file : files) {
			String fileName = file.getName();
			if (file.isDirectory()) {
				URI childUri = new URI(uri + "/" + fileName);
				map.putAll(loadFromDir(childUri, suffix));
			} else {
				String content = FileUtils.readFile(file);
				if (map.containsKey(fileName)) {
					map.put(uri.getPath() + "." + fileName, content);
				} else {
					map.put(fileName, content);
				}
			}
		}

		return map;
	}

	/**
	 * 移除URI中的schema指定路径
	 * <pre>
	 *      jar:file:/Users/path -> /Users/path
	 *
	 * @param uri
	 * @param schemaName
	 * @return
	 * @throws URISyntaxException
	 */
	private static URI getSchemeSpecificPart(URI uri, String schemaName) throws URISyntaxException {
		String schema = uri.getScheme();

		if (StringUtils.equalsIgnoreCase(schema, schemaName)) {
			return new URI(uri.getSchemeSpecificPart());
		}

		if (StringUtils.isEmpty(schema)) {
			throw new IllegalArgumentException("Not found scheme: " + schemaName);
		}

		URI newUri = new URI(uri.getSchemeSpecificPart());
		return getSchemeSpecificPart(newUri, schemaName);
	}

	/**
	 * 格式化路径
	 * <pre>
	 *      /dirName/ -> dirName
	 *
	 * @param path
	 * @return
	 */
	private static String formatPath(String path) {
		path = path.trim();

		if (path.startsWith("/")) {
			path = path.substring(1);
		}

		if (path.endsWith("/")) {
			path = path.substring(0, path.length() - 1);
		}

		return path;
	}
}
