package com.chinapex.label.util

import org.apache.hadoop.fs._

import java.io._

object HdfsUtil {

  def objectSerialize[T](fs: FileSystem, obj: T, dfsPath: Path): Unit = {
    var file: FSDataOutputStream = null
    var oos: ObjectOutputStream = null
    try {
      file = fs.create(dfsPath, true)
      oos = new ObjectOutputStream(file)
      oos.writeObject(obj)
    } finally {
      if (null != oos) {
        oos.close()
      }

      if (null != file) {
        file.close()
      }
    }
  }

  def objectSerialize[T](obj: T, desPath: String): Unit = {
    var file: FileOutputStream = null
    var oos: ObjectOutputStream = null
    try {
      file = new FileOutputStream(desPath)
      oos = new ObjectOutputStream(file)
      oos.writeObject(obj)
    } finally {
      if (null != oos) {
        oos.close()
      }

      if (null != file) {
        file.close()
      }
    }
  }

  def objectSerialize[T](fs: FileSystem, obj: T, path: String): Unit = {
    objectSerialize(fs, obj, new Path(path))
  }

  def deserializeObject[T](fs: FileSystem, dfsPath: Path): T = {
    if (!fs.exists(dfsPath)) {
      throw new Exception("file not found exception at path of '" + dfsPath.toString)
    }
    var file: FSDataInputStream = null
    var ois: ObjectInputStream = null
    try {
      file = fs.open(dfsPath)
      ois = new ObjectInputStream(file)
      ois.readObject().asInstanceOf[T]
    } finally {
      if (null != ois) {
        ois.close()
      }
      if (null != file) {
        file.close()
      }
    }
  }

  def deserializeObject[T](fs: FileSystem, path: String): T = {
    deserializeObject(fs, new Path(path))
  }

  /**
   * 向HDFS写
   *
   * @param fs
   * @param address
   * @param content
   * @param overwrite
   * @param charset
   */
  def write(fs: FileSystem, address: Path, content: String, overwrite: Boolean, charset: String): Unit = {
    var writer: OutputStreamWriter = null
    var file: FSDataOutputStream = null
    var buffer: BufferedWriter = null
    try {
      file = fs.create(address, overwrite)
      writer = new OutputStreamWriter(file, charset)
      buffer = new BufferedWriter(writer)
      buffer.write(content)
      buffer.flush()
    } finally {
      if (null != buffer) {
        buffer.close()
      }
      if (null != writer) {
        writer.close()
      }
      if (null != file) {
        file.close()
      }
    }
  }

  /**
   * 向HDFS写
   *
   * @param fs
   * @param address
   * @param content
   * @param overwrite
   */
  def write(fs: FileSystem, address: Path, content: String, overwrite: Boolean): Unit = {
    write(fs, address, content, overwrite, "utf-8")
  }

  /**
   * 向HDFS写
   *
   * @param fs
   * @param address
   * @param content
   * @param overwrite
   */
  def write(fs: FileSystem, address: String, content: String, overwrite: Boolean): Unit = {
    write(fs, new Path(address), content, overwrite, "utf-8")
  }

  /**
   * 向HDFS写
   *
   * @param fs
   * @param address
   * @param content
   */
  def write(fs: FileSystem, address: Path, content: String): Unit = {
    write(fs, address, content, true, "utf-8")
  }

  /**
   * 向HDFS写
   *
   * @param fs
   * @param address
   * @param content
   */
  def write(fs: FileSystem, address: String, content: String): Unit = {
    write(fs, new Path(address), content, true, "utf-8")
  }


  /**
   * 向HDFS写
   *
   * @param fs
   * @param address
   * @param content
   * @param overwrite
   * @param charset
   */
  def write(fs: FileSystem, address: Path, content: Seq[String], overwrite: Boolean, charset: String): Unit = {
    var writer: OutputStreamWriter = null
    var file: FSDataOutputStream = null
    var buffer: BufferedWriter = null
    try {
      file = fs.create(address, overwrite)
      writer = new OutputStreamWriter(file, charset)
      buffer = new BufferedWriter(writer)
      content.foreach(c => {
        buffer.write(c)
        buffer.newLine()
      })
      buffer.flush()
    } finally {
      if (null != buffer) {
        buffer.close()
      }
      if (null != writer) {
        writer.close()
      }
      if (null != file) {
        file.close()
      }
    }
  }

  /**
   * 向HDFS写
   *
   * @param fs
   * @param address
   * @param content
   * @param overwrite
   */
  def write(fs: FileSystem, address: Path, content: Seq[String], overwrite: Boolean): Unit = {
    write(fs, address, content, overwrite, "utf-8")
  }

  /**
   * 向HDFS写
   *
   * @param fs
   * @param address
   * @param content
   * @param overwrite
   */
  def write(fs: FileSystem, address: String, content: Seq[String], overwrite: Boolean): Unit = {
    write(fs, new Path(address), content, overwrite, "utf-8")
  }

  /**
   * 向HDFS写
   *
   * @param fs
   * @param address
   * @param content
   */
  def write(fs: FileSystem, address: Path, content: Seq[String]): Unit = {
    write(fs, address, content, true, "utf-8")
  }

  /**
   * 向HDFS写
   *
   * @param fs
   * @param address
   * @param content
   */
  def write(fs: FileSystem, address: String, content: Seq[String]): Unit = {
    write(fs, new Path(address), content, true, "utf-8")
  }


  /**
   * 向HDFS写
   *
   * @param fs
   * @param address
   * @param content
   * @param overwrite
   * @param charset
   */
  def write(fs: FileSystem, address: Path, content: List[String], overwrite: Boolean, charset: String): Unit = {
    var writer: OutputStreamWriter = null
    var file: FSDataOutputStream = null
    var buffer: BufferedWriter = null
    try {
      file = fs.create(address, overwrite)
      writer = new OutputStreamWriter(file, charset)
      buffer = new BufferedWriter(writer)
      content.foreach(c => {
        buffer.write(c)
        buffer.newLine()
      })
      buffer.flush()
    } finally {
      if (null != buffer) {
        buffer.close()
      }
      if (null != writer) {
        writer.close()
      }
      if (null != file) {
        file.close()
      }
    }
  }

  /**
   * 向HDFS写
   *
   * @param fs
   * @param address
   * @param content
   * @param overwrite
   */
  def write(fs: FileSystem, address: Path, content: List[String], overwrite: Boolean): Unit = {
    write(fs, address, content, overwrite, "utf-8")
  }

  /**
   * 向HDFS写
   *
   * @param fs
   * @param address
   * @param content
   * @param overwrite
   */
  def write(fs: FileSystem, address: String, content: List[String], overwrite: Boolean): Unit = {
    write(fs, new Path(address), content, overwrite, "utf-8")
  }

  /**
   * 向HDFS写
   *
   * @param fs
   * @param address
   * @param content
   */
  def write(fs: FileSystem, address: Path, content: List[String]): Unit = {
    write(fs, address, content, true, "utf-8")
  }

  /**
   * 向HDFS写
   *
   * @param fs
   * @param address
   * @param content
   */
  def write(fs: FileSystem, address: String, content: List[String]): Unit = {
    write(fs, new Path(address), content, true, "utf-8")
  }


  /**
   * 向HDFS写
   *
   * @param fs
   * @param address
   * @param content
   * @param overwrite
   * @param charset
   */
  def write(fs: FileSystem, address: Path, content: java.util.List[String], overwrite: Boolean, charset: String): Unit = {
    var writer: OutputStreamWriter = null
    var file: FSDataOutputStream = null
    var buffer: BufferedWriter = null
    try {
      file = fs.create(address, overwrite)
      writer = new OutputStreamWriter(file, charset)
      buffer = new BufferedWriter(writer)
      val len = content.size()
      for (i <- 0 until len) {
        buffer.write(content.get(i))
        buffer.newLine()
      }
      buffer.flush()
    } finally {
      if (null != buffer) {
        buffer.close()
      }
      if (null != writer) {
        writer.close()
      }
      if (null != file) {
        file.close()
      }
    }
  }

  /**
   * 向HDFS写
   *
   * @param fs
   * @param address
   * @param content
   * @param overwrite
   */
  def write(fs: FileSystem, address: Path, content: java.util.List[String], overwrite: Boolean): Unit = {
    write(fs, address, content, overwrite, "utf-8")
  }

  /**
   * 向HDFS写
   *
   * @param fs
   * @param address
   * @param content
   * @param overwrite
   */
  def write(fs: FileSystem, address: String, content: java.util.List[String], overwrite: Boolean): Unit = {
    write(fs, new Path(address), content, overwrite, "utf-8")
  }

  /**
   * 向HDFS写
   *
   * @param fs
   * @param address
   * @param content
   */
  def write(fs: FileSystem, address: Path, content: java.util.List[String]): Unit = {
    write(fs, address, content, true, "utf-8")
  }

  /**
   * 向HDFS写
   *
   * @param fs
   * @param address
   * @param content
   */
  def write(fs: FileSystem, address: String, content: java.util.List[String]): Unit = {
    write(fs, new Path(address), content, true, "utf-8")
  }

  def readTxt(fs: FileSystem, address: Path, charset: String): Seq[String] = {
    var reader: InputStreamReader = null
    var buffer: BufferedReader = null
    var file: FSDataInputStream = null
    try {
      file = fs.open(address)
      reader = new InputStreamReader(file, charset)
      buffer = new BufferedReader(reader)
      var line: String = buffer.readLine()
      var txt = Seq.empty[String]
      while (null != line) {
        txt = txt.:+(line)
        line = buffer.readLine()
      }
      return txt
    } finally {
      if (null != buffer) {
        buffer.close()
      }
      if (null != reader) {
        reader.close()
      }
      if (null != file) {
        file.close()
      }
    }
  }

  def readTxt(fs: FileSystem, address: String, charset: String): Seq[String] = {
    readTxt(fs, new Path(address), charset)
  }

  def readTxt(fs: FileSystem, address: String): Seq[String] = {
    readTxt(fs, new Path(address), "utf-8")
  }

  def readTxt(fs: FileSystem, address: Path): Seq[String] = {
    readTxt(fs, address, "utf-8")
  }


  /**
   * 读取文本文件的第一行
   *
   * @param fs
   * @param address
   * @param charset
   * @return
   */
  def readLine(fs: FileSystem, address: Path, charset: String): String = {
    var reader: InputStreamReader = null
    var buffer: BufferedReader = null
    var file: FSDataInputStream = null
    try {
      file = fs.open(address)
      reader = new InputStreamReader(file, charset)
      buffer = new BufferedReader(reader)
      buffer.readLine()
    } finally {
      if (null != buffer) {
        buffer.close()
      }
      if (null != reader) {
        reader.close()
      }
      if (null != file) {
        file.close()
      }
    }
  }

  def readLine(fs: FileSystem, address: Path): String = {
    readLine(fs, address, "utf-8")
  }

  def readLine(fs: FileSystem, address: String, charset: String): String = {
    readLine(fs, new Path(address), charset)
  }

  def readLine(fs: FileSystem, address: String): String = {
    readLine(fs, new Path(address), "utf-8")
  }

  /**
   * 创建个一个空文件
   *
   * @param fs
   * @param address
   * @return
   */
  def touchEmpty(fs: FileSystem, address: Path): Boolean = {
    var file: FSDataOutputStream = null
    try {
      file = fs.create(address, true)
      return true
    } finally {
      file.close()
    }
    return false
  }


  /**
   * 删除文件
   *
   * @param fs
   * @param address
   */
  def remove(fs: FileSystem, address: Path): Boolean = {
    if (fs.exists(address)) {
      return fs.delete(address, true)
    }
    return false
  }

  /**
   * 删除文件
   *
   * @param fs
   * @param address
   */
  def remove(fs: FileSystem, address: String): Boolean = {
    remove(fs, new Path(address))
  }

  /**
   * dir预处理，不存在，则创建
   * 如果存在同名的文件，则抛出异常
   *
   * @param fs
   * @param dirPath
   * @return
   */
  def dirPrepare(fs: FileSystem, dirPath: Path): Boolean = {
    if (fs.exists(dirPath)) {
      if (fs.isDirectory(dirPath)) {
        return true
      } else {
        throw new FileAlreadyExistsException("The path " + dirPath + " is a exists file.")
      }
    }
    fs.mkdirs(dirPath)
    return fs.exists(dirPath)
  }

  def dirPrepare(fs: FileSystem, dirUrl: String): Boolean = {
    dirPrepare(fs, new Path(dirUrl))
  }

  /**
   * 移动hdfs中的文件，如果目标文件存在，根据@param delExistDesFile是否为true则将目标文件删除
   *
   * @param sourcePath      目标源文件
   * @param desPath         目标文件
   * @param delExistDesFile 如果目标文件存在，则先删除, 此参数为true时，慎用
   * @return
   */
  def mv(fs: FileSystem, sourcePath: Path, desPath: Path, delExistDesFile: Boolean): Boolean = {
    if (delExistDesFile && fs.exists(desPath)) {
      fs.delete(sourcePath, false)
    }
    fs.rename(sourcePath, desPath)
    if (fs.exists(sourcePath)) {
      return false
    } else {
      return true
    }
  }

  def mv(fs: FileSystem, sourcePath: Path, desUrl: String): Boolean = {
    mv(fs, sourcePath, new Path(desUrl), false)
  }

  def mv(fs: FileSystem, sourceUrl: String, desPath: Path): Boolean = {
    mv(fs, new Path(sourceUrl), desPath, false)
  }

  def mv(fs: FileSystem, sourceUrl: String, desUrl: String): Boolean = {
    mv(fs, new Path(sourceUrl), new Path(desUrl), false)
  }

  def mv(fs: FileSystem, sourcePath: Path, desUrl: String, del: Boolean): Boolean = {
    mv(fs, sourcePath, new Path(desUrl), del)
  }

  def mv(fs: FileSystem, sourceUrl: String, desPath: Path, del: Boolean): Boolean = {
    mv(fs, new Path(sourceUrl), desPath, del)
  }

  def mv(fs: FileSystem, sourceUrl: String, desUrl: String, del: Boolean): Boolean = {
    mv(fs, new Path(sourceUrl), new Path(desUrl), del)
  }

  /**
   * 判断文件或目录是否存在
   *
   * @param fs
   * @param path
   * @return
   */
  def exist(fs: FileSystem, path: Path): Boolean = {
    fs.exists(path)
  }

  /**
   * 判断文件或目录是否存在
   *
   * @param fs
   * @param path
   * @return
   */
  def exist(fs: FileSystem, path: String): Boolean = {
    fs.exists(new Path(path))
  }

  /**
   * 判断文件是否是目录
   *
   * @param fs
   * @param path
   * @return
   */
  def isDirectory(fs: FileSystem, path: Path): Boolean = {
    fs.isDirectory(path)
  }

  /**
   * 判断文件是否是目录
   *
   * @param fs
   * @param path
   * @return
   */
  def isDirectory(fs: FileSystem, path: String): Boolean = {
    fs.isDirectory(new Path(path))
  }


  /**
   * 用源文件或目录替换目标文件或目录
   * 目录文件存在，直接替换。目标文件不存在，直接将源文件移入目标文件
   * 源文件不存在，抛出异常
   *
   * @param fs
   * @param source
   * @param des
   */
  def replace(fs: FileSystem, source: Path, des: Path): Boolean = {
    if (!fs.exists(source)) {
      throw new FileNotFoundException(s"The file of $source not found.")
    }

    var ret = false
    if (fs.exists(des)) {
      val deletingPath = new Path(s"${des.toString}.deleting")
      //rename destination to deleting
      fs.rename(des, deletingPath)
      //rename source to destination
      fs.rename(source, des)
      //check new destination exist and source not exist, replace okay.
      if (fs.exists(des) && !fs.exists(source)) {
        fs.delete(deletingPath, true)
        ret = true
      } else {
        //rollback
        fs.rename(des, source)
        fs.rename(deletingPath, des)
      }
    } else {
      //rename source to destination
      fs.rename(source, des)
      ret = true
    }
    ret
  }

  /**
   * 用源文件或目录替换目标文件或目录
   * 目录文件存在，直接替换。目标文件不存在，直接将源文件移入目标文件
   * 源文件不存在，抛出异常
   *
   * @param fs
   * @param source
   * @param des
   */
  def replace(fs: FileSystem, source: String, des: String): Boolean = {
    replace(fs, new Path(source), new Path(des))
  }

}
