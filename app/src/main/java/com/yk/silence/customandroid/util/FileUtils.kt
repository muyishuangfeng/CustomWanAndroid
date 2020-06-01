package com.yk.silence.customandroid.util

import android.os.Environment
import android.util.Log
import java.io.*
import java.text.NumberFormat
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream


object FileUtils {


    fun isSdCardExist(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }


    /**
     * 解压
     */
    @Throws(IOException::class)
    fun unzip(`is`: InputStream?, dir: String?) {
        val dest = File(dir)
        if (!dest.exists()) {
            dest.mkdirs()
        }
        if (!dest.isDirectory) throw IOException("Invalid Unzip destination $dest")
        if (null == `is`) {
            throw IOException("InputStream is null")
        }
        val zip = ZipInputStream(`is`)
        var ze: ZipEntry
        while (zip.nextEntry.also { ze = it } != null) {
            val path = (dest.absolutePath
                    + File.separator + ze.name)
            val zeName = ze.name
            val cTail = zeName[zeName.length - 1]
            if (cTail == File.separatorChar) {
                val file = File(path)
                if (!file.exists()) {
                    if (!file.mkdirs()) {
                        throw IOException("Unable to create folder $file")
                    }
                }
                continue
            }
            val fout = FileOutputStream(path)
            val bytes = ByteArray(1024)
            var c: Int
            while (zip.read(bytes).also { c = it } != -1) {
                fout.write(bytes, 0, c)
            }
            zip.closeEntry()
            fout.close()
        }
    }

    fun getFileSize(fileSize: Number): String? {
        val ddf1 = NumberFormat.getNumberInstance()
        //保留小数点后两位
        ddf1.maximumFractionDigits = 2
        val size: Double = fileSize.toDouble()
        val sizeDisplay: String
        sizeDisplay = if (size > 1048576.0) {
            val result = size / 1048576.0
            ddf1.format(result) + " MB"
        } else if (size > 1024) {
            val result = size / 1024
            ddf1.format(result) + " KB"
        } else {
            ddf1.format(size) + " B"
        }
        return sizeDisplay
    }

    fun getFileSize(size: Long): String? {
        val ddf1 = NumberFormat.getNumberInstance()
        //保留小数点后两位
        ddf1.maximumFractionDigits = 2
        val sizeDisplay: String
        sizeDisplay = if (size > 1048576.0) {
            val result = size / 1048576.0
            ddf1.format(result)
        } else if (size > 1024) {
            val result = size / 1024.toDouble()
            ddf1.format(result)
        } else {
            ddf1.format(size)
        }
        return sizeDisplay
    }

    /**
     * 获取文件扩展名
     */
    fun getExtensionName(filename: String?): String? {
        if (filename != null && filename.isNotEmpty()) {
            val dot = filename.lastIndexOf('.')
            if (dot > -1 && dot < filename.length - 1) {
                return filename.substring(dot + 1)
            }
        }
        return ""
    }

    /**
     * 获取文件名
     */
    fun getFileNameFromPath(filepath: String?): String? {
        if (filepath != null && filepath.isNotEmpty()) {
            val sep = filepath.lastIndexOf('/')
            if (sep > -1 && sep < filepath.length - 1) {
                return filepath.substring(sep + 1)
            }
        }
        return filepath
    }

    /**
     * 判断文件是否存在
     *
     * @param filePath 文件路径
     * @return `true`: 存在<br></br>`false`: 不存在
     */
    fun isFileExists(filePath: String): Boolean {
        return isFileExists(getFileByPath(filePath))
    }

    /**
     * 判断文件是否存在
     *
     * @param file 文件
     * @return `true`: 存在<br></br>`false`: 不存在
     */
    fun isFileExists(file: File?): Boolean {
        return file != null && file.exists()
    }

    /**
     * 根据文件路径获取文件
     *
     * @param filePath 文件路径
     * @return 文件
     */
    fun getFileByPath(filePath: String): File? {
        return if (isSpace(filePath)) null else File(filePath)
    }

    /**
     * 创建文件夹
     */
    fun createFile(path: kotlin.String) {
        val file = File(path)
        if (!file.exists()) {
            file.mkdirs()
        }
    }

    /**
     * 判断字符串是否为null或全为空格
     *
     * @param s 待校验字符串
     * @return `true`: null或全空格<br></br> `false`: 不为null且不全空格
     */
    private fun isSpace(s: String?): Boolean {
        return s == null || s.trim { it <= ' ' }.isEmpty()
    }


    /**
     * 创建文件
     *
     * @param filePath 文件地址
     * @param fileName 文件名
     * @return
     */
    fun createFile(filePath: String, fileName: String): Boolean {
        val strFilePath = filePath + fileName
        val file = File(filePath)
        if (!file.exists()) {
            /**  注意这里是 mkdirs()方法  可以创建多个文件夹  */
            file.mkdirs()
        }
        val subfile = File(strFilePath)
        if (!subfile.exists()) {
            try {
                return subfile.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            return true
        }
        return false
    }

    /**
     * 遍历文件夹下的文件
     *
     * @param file 地址
     */
    private fun getFile(file: File): List<File>? {
        val list: MutableList<File> = ArrayList()
        val fileArray = file.listFiles()
        if (fileArray == null) {
            return null
        } else {
            for (f in fileArray) {
                if (f.isFile) {
                    list.add(0, f)
                } else {
                    getFile(f)
                }
            }
        }
        return list
    }

    /**
     * 删除文件
     *
     * @param filePath 文件地址
     * @return
     */
    fun deleteFiles(filePath: String?): Boolean {
        val files =
            getFile(File(filePath))
        if (files!!.isNotEmpty()) {
            for (i in files.indices) {
                val file = files[i]
                /**  如果是文件则删除  如果都删除可不必判断   */
                if (file.isFile) {
                    file.delete()
                }
            }
        }
        return true
    }

    /**
     * 向文件中添加内容
     *
     * @param strcontent 内容
     * @param filePath   地址
     * @param fileName   文件名
     */
    fun writeToFile(
        strcontent: String,
        filePath: String,
        fileName: String
    ) { //生成文件夹之后，再生成文件，不然会出错
        val strFilePath = filePath + fileName
        // 每次写入时，都换行写
        val subfile = File(strFilePath)
        var raf: RandomAccessFile? = null
        try {
            /**   构造函数 第二个是读写方式     */
            raf = RandomAccessFile(subfile, "rw")
            /**  将记录指针移动到该文件的最后   */
            raf.seek(subfile.length())
            /** 向文件末尾追加内容   */
            raf.write(strcontent.toByteArray())
            raf.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * 修改文件内容（覆盖或者添加）
     *
     * @param path    文件地址
     * @param content 覆盖内容
     * @param append  指定了写入的方式，是覆盖写还是追加写(true=追加)(false=覆盖)
     */
    fun modifyFile(
        path: String?,
        content: String?,
        append: Boolean
    ) {
        try {
            val fileWriter = FileWriter(path, append)
            val writer = BufferedWriter(fileWriter)
            writer.append(content)
            writer.flush()
            writer.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * 读取文件内容
     *
     * @param filePath 地址
     * @param filename 名称
     * @return 返回内容
     */
    fun getString(filePath: String, filename: String): String {
        var inputStream: FileInputStream? = null
        try {
            inputStream = FileInputStream(File(filePath + filename))
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        var inputStreamReader: InputStreamReader? = null
        try {
            inputStreamReader = InputStreamReader(inputStream, "UTF-8")
        } catch (e1: UnsupportedEncodingException) {
            e1.printStackTrace()
        }
        val reader = BufferedReader(inputStreamReader)
        val sb = StringBuffer("")
        var line: String = ""
        try {
            while (reader.readLine().also { line = it } != null) {
                sb.append(line)
                sb.append("\n")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return sb.toString()
    }

    /**
     * 重命名文件
     *
     * @param oldPath 原来的文件地址
     * @param newPath 新的文件地址
     */
    fun renameFile(oldPath: String?, newPath: String?) {
        val oleFile = File(oldPath)
        val newFile = File(newPath)
        //执行重命名
        oleFile.renameTo(newFile)
    }




    /**
     * 复制文件
     * @param oldPath 源文件路径
     * @param newPath 目标文件路径
     */
    fun copyFile(oldPath: String, newPath: String): Boolean {
        val oldFile = File(oldPath)
        val newFile = File(newPath)
        newFile.deleteOnExit()
        newFile.createNewFile()
        var c = -1
        val buffer = ByteArray(1024 * 1000)
        val inputStream = oldFile.inputStream()
        val now = System.currentTimeMillis()
        while ({ c = inputStream.read(buffer);c }() > 0) {
            newFile.appendBytes(buffer.copyOfRange(0, c))
        }
        Log.e("TAG","复制完毕，耗时${(System.currentTimeMillis() - now) / 1000}秒")
        return true
    }
}



