package com.demo.fileprocessing.utils.azure;

public class PathUtils {

  public static String getContainerName(String path) {
    return path.substring(0, path.indexOf("/"));
  }

  public static String getBlobName(String path) {
    return path.substring(path.indexOf("/") + 1);
  }
}
