package ${package}.web.pasm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

public final class FileUtils {
    private final static String NEWLINE = System.getProperty("line.separator");
    private final static String RootPath = System.getProperty("user.dir");
    private static int bufferSize = 81920;

    /**
     * @param filePath
     * @return
     * @throws IOException
     */
    public static String readFile(String filePath) throws IOException {
        return readFile(filePath, "utf-8");
    }

    /**
     * @param filePath
     * @param charsetName
     * @return
     * @throws IOException
     */
    public static String readFile(String filePath, String charsetName) throws IOException {
        return readFile(new File(filePath), charsetName);
    }

    /**
     * @param file
     * @return
     * @throws IOException
     */
    public static String readFile(File file) throws IOException {
        return readFile(file, "utf-8");
    }

    /**
     * @param file
     * @param charsetName
     * @return
     * @throws IOException
     */
    public static String readFile(File file, String charsetName) throws IOException {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            return readStream(inputStream, charsetName);
        } finally {
            Utils.close(inputStream);
        }
    }

    /**
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static String readStream(InputStream inputStream) throws IOException {
        return readStream(inputStream, "utf-8");
    }

    /**
     * @param inputStream
     * @param charsetName
     * @return
     * @throws IOException
     */
    public static String readStream(InputStream inputStream, String charsetName) throws IOException {
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;

        try {
            inputStreamReader = new InputStreamReader(inputStream, charsetName);
                bufferedReader = new BufferedReader(inputStreamReader, bufferSize);

                StringBuilder builder = new StringBuilder();

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    builder.append(line);
                    builder.append(NEWLINE);
            }

            return builder.toString();
        } finally {
            Utils.close(bufferedReader,
                    inputStreamReader);
        }
    }

    /**
     * @param filePath
     * @param text
     * @param append
     * @throws IOException
     */
    public static void writeFile(String filePath, String text, boolean append) throws IOException {
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;

        try {
            File file = new File(filePath);
            if(!file.getParentFile().exists())
                file.getParentFile().mkdirs();
            fileWriter = new FileWriter(filePath, append);
            bufferedWriter = new BufferedWriter(fileWriter, bufferSize);
            bufferedWriter.write(text);
        } finally {
            Utils.close(bufferedWriter,
                    fileWriter);
        }
    }

    /**
     * @param sourceFilePath
     * @param targetFilePath
     * @throws IOException
     */
    public static void copyFile(String sourceFilePath, String targetFilePath) throws IOException {
        File sourceFile = new File(sourceFilePath);
        File targetFile = new File(targetFilePath);
        copyFile(sourceFile, targetFile);
    }

    /**
     * @param sourceFile
     * @param targetFile
     * @throws IOException
     */
    public static void copyFile(File sourceFile, File targetFile) throws IOException {
        InputStream sourceStream = null;
        OutputStream targetStream = null;

        try {
            sourceStream = new BufferedInputStream(new FileInputStream(sourceFile));
            targetStream = new BufferedOutputStream(new FileOutputStream(targetFile));

            byte[] buffer = new byte[bufferSize];

            int i;
            while ((i = sourceStream.read(buffer)) != -1) {
                targetStream.write(buffer, 0, i);
            }
        } finally {
            Utils.close(sourceStream,
                    targetStream);
        }
    }

    public static String getFilePath(String filename) {
        return String.format("%s/%s/%s", RootPath, "files", filename);
    }


    /**
     * 下载文件,并删除文件
     *
     * @param file
     * @param response
     */
    public static void downLoad(File file, HttpServletResponse response, HttpServletRequest request) {
        downLoad(file, true, null, response, request);
    }

    /**
     * 下载文件,并删除文件
     *
     * @param file
     * @param showfilename
     * @param response
     */
    public static void downLoad(File file, String showfilename, HttpServletResponse response, HttpServletRequest request) {
        downLoad(file, true, showfilename, response, request);
    }

    /**
     * 下载文件
     *
     * @param file
     * @param deleFile 是否删除文件
     * @param response
     */
    public static void downLoad(File file, boolean deleFile, String showfilename, HttpServletResponse response, HttpServletRequest request) {
        try {
            // 读到流中
            InputStream inStream = new FileInputStream(file);// 文件的存放路径
            // 设置输出的格式
            response.reset();
            response.setContentType("bin");
            String[] strArry = file.getName().split("\\.");
            boolean flag = request.getHeader("User-Agent").indexOf("like Gecko") > 0;
            String filename = showfilename == null ? file.getName() : showfilename + "." + strArry[strArry.length - 1];
            if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0 || flag) {
                filename = URLEncoder.encode(filename, "UTF-8");
            } else {
                filename = new String(filename.getBytes("UTF-8"), "ISO8859-1");
            }
            response.addHeader("Content-Disposition", "attachment; filename=\"" + filename);
            // 循环取出流中的数据
            byte[] b = new byte[1024];
            int len;
            try {
                while ((len = inStream.read(b)) > 0)
                    response.getOutputStream().write(b, 0, len);
                inStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (deleFile) {
                file.delete();
            }
        }
    }

    /**
     * 下载文件
     *
     * @param file
     * @param deleFile
     * @param response
     */
    public static void downLoad(File file, boolean deleFile, HttpServletResponse response, HttpServletRequest request) {
        downLoad(file, deleFile, null, response, request);
    }

    /**
     * 下载文件,并删除文件
     *
     * @param filePath
     * @param response
     */
    public static void downLoad(String filePath, HttpServletResponse response, HttpServletRequest request) {
        File file = new File(filePath);
        downLoad(file, true, null, response, request);
    }

    /**
     * 下载文件,并删除文件
     *
     * @param filePath
     * @param showfilename
     * @param response
     */
    public static void downLoad(String filePath, String showfilename, HttpServletResponse response, HttpServletRequest request) {
        File file = new File(filePath);
        downLoad(file, true, showfilename, response, request);
    }

}
