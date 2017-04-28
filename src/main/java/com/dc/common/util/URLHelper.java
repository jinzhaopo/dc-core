package com.dc.common.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.web.util.UrlPathHelper;

/**
 * 
 * @ClassName: URLHelper
 * @Description: URI帮助类
 * @author: jinzhaopo
 * @version: V1.0
 * @date: 2017年4月22日 下午3:04:28
 */
public class URLHelper {
	/**
	 * 获得页号
	 * 
	 * @param request
	 * @return
	 */
	public static int getPageNo(HttpServletRequest request) {
		return getPageNo(getURI(request));
	}

	/**
	 * 获得路径信息
	 * 
	 * @param request
	 * @return
	 */
	public static String[] getPaths(HttpServletRequest request) {
		return getPaths(getURI(request));
	}

	/**
	 * 获得路径参数
	 * 
	 * @param request
	 * @return
	 */
	public static String[] getParams(HttpServletRequest request) {
		return getParams(getURI(request));
	}

	public static String getURI(HttpServletRequest request) {
		UrlPathHelper helper = new UrlPathHelper();
		String uri = helper.getOriginatingRequestUri(request);
		String ctx = helper.getOriginatingContextPath(request);
		if (!StringUtils.isBlank(ctx)) {
			return uri.substring(ctx.length());
		} else {
			return uri;
		}
	}

	/**
	 * 获得翻页信息
	 * 
	 * @param request
	 * @return
	 */
	public static PageInfo getPageInfo(HttpServletRequest request) {
		UrlPathHelper helper = new UrlPathHelper();
		String uri = helper.getOriginatingRequestUri(request);
		String queryString = helper.getOriginatingQueryString(request);
		return getPageInfo(uri, queryString);
	}

	/**
	 * 获得页号
	 * 
	 * @param uri
	 *            URI {@link HttpServletRequest#getRequestURI()}
	 * @return
	 */
	public static int getPageNo(String uri) {
		if (uri == null) {
			throw new IllegalArgumentException("URI can not be null");
		}
		if (!uri.startsWith("/")) {
			throw new IllegalArgumentException("URI must start width '/'");
		}
		int pageNo = 1;
		int bi = uri.indexOf("page");
		int pi = uri.indexOf(".");
		if (bi != -1) {
			String pageNoStr = uri.substring(bi + 4, pi);
			try {
				pageNo = Integer.valueOf(pageNoStr);
			} catch (Exception e) {
			}
		}
		return pageNo;
	}

	/**
	 * 获得路径数组
	 * 
	 * @param uri
	 *            URI {@link HttpServletRequest#getRequestURI()}
	 * @return
	 */
	public static String[] getPaths(String uri) {
		if (uri == null) {
			throw new IllegalArgumentException("URI can not be null");
		}
		if (!uri.startsWith("/")) {
			throw new IllegalArgumentException("URI must start width '/'");
		}
		int bi = uri.indexOf("page");
		// 获得路径信息
		String pathStr = null;
		if (bi != -1) {
			pathStr = uri.substring(0, bi);
			// } else if (mi != -1) {
			// pathStr = uri.substring(0, mi);
			// } else if (pi != -1) {
			// pathStr = uri.substring(0, pi);
		} else {
			pathStr = uri;
		}
		String[] paths = StringUtils.split(pathStr, '/');
		return paths;
	}

	/**
	 * 获得路径参数
	 * 
	 * @param uri
	 *            URI {@link HttpServletRequest#getRequestURI()}
	 * @return
	 */
	public static String[] getParams(String uri) {
		if (uri == null) {
			throw new IllegalArgumentException("URI can not be null");
		}
		if (!uri.startsWith("/")) {
			throw new IllegalArgumentException("URI must start width '/'");
		}
		int mi = uri.indexOf("-");
		int pi = uri.indexOf(".");
		String[] params;
		if (mi != -1) {
			String paramStr;
			if (pi != -1) {
				paramStr = uri.substring(mi, pi);
			} else {
				paramStr = uri.substring(mi);
			}
			params = new String[StringUtils.countMatches(paramStr, "-")];
			int fromIndex = 1;
			int nextIndex = 0;
			int i = 0;
			while ((nextIndex = paramStr.indexOf("-", fromIndex)) != -1) {
				params[i++] = paramStr.substring(fromIndex, nextIndex);
				fromIndex = nextIndex + 1;
			}
			params[i++] = paramStr.substring(fromIndex);
		} else {
			params = new String[0];
		}
		return params;
	}

	/**
	 * 获得URL信息
	 * 
	 * @param uri
	 *            URI {@link HttpServletRequest#getRequestURI()}
	 * @param queryString
	 *            查询字符串 {@link HttpServletRequest#getQueryString()}
	 * @return
	 */
	public static PageInfo getPageInfo(String uri, String queryString) {
		if (uri == null) {
			return null;
		}
		if (!uri.startsWith("/")) {
			throw new IllegalArgumentException("URI must start width '/'");
		}
		int bi = uri.indexOf("_");
		int mi = uri.indexOf("-");
		int pi = uri.indexOf(".");
		int lastSpt = uri.lastIndexOf("/") + 1;
		String url;
		if (!StringUtils.isBlank(queryString)) {
			url = uri + "?" + queryString;
		} else {
			url = uri;
		}
		// 翻页前半部
		String urlFormer;
		if (bi != -1) {
			urlFormer = uri.substring(lastSpt, bi);
		} else if (mi != -1) {
			urlFormer = uri.substring(lastSpt, mi);
		} else if (pi != -1) {
			urlFormer = uri.substring(lastSpt, pi);
		} else {
			urlFormer = uri.substring(lastSpt);
		}
		// 翻页后半部
		String urlLater;
		if (mi != -1) {
			urlLater = url.substring(mi);
		} else if (pi != -1) {
			urlLater = url.substring(pi);
		} else {
			urlLater = url.substring(uri.length());
		}
		String href = url.substring(lastSpt);
		return new PageInfo(href, urlFormer, urlLater);
	}

	/**
	 * 获得URL信息
	 * 
	 * @param uri
	 *            URI {@link HttpServletRequest#getRequestURI()}
	 * @param queryString
	 *            查询字符串 {@link HttpServletRequest#getQueryString()}
	 * @return
	 */
	public static StorePageInfo getStorePageInfo(String uri, String queryString) {
		if (uri == null) {
			return null;
		}
		if (!uri.startsWith("/")) {
			throw new IllegalArgumentException("URI must start width '/'");
		}
		if (uri.contains(".")) {
			uri = uri.substring(0, uri.indexOf("."));
		}
		String pagePath = null;
		String[] path = uri.split("/");
		if (path[path.length - 1].startsWith("page")) {
			pagePath = path[path.length - 1];
			path = ArrayUtils.remove(path, path.length - 1);
		}
		// 分类路径
		String catePre, cateLatter = null;
		if (path.length >= 3) {
			if (path[2].contains("-")) {
				catePre = path[2].split("-")[0];
				cateLatter = path[2].split("-")[1];
			} else {
				catePre = path[2];
			}
		} else {
			catePre = null;
		}
		// 区域路径
		String reginPath;
		if (path.length >= 4) {
			reginPath = path[3];
		} else {
			reginPath = null;
		}

		// 排序路径
		String sortPath;
		if (path.length >= 5) {
			sortPath = path[4];
		} else {
			sortPath = null;
		}
		return new StorePageInfo(catePre, cateLatter, reginPath, sortPath, pagePath, path.length);
	}

	/**
	 * 
	 * @param queryString
	 *            查询字符串 {@link HttpServletRequest#getQueryString()}
	 * @param info
	 * @param type
	 *            1:替换分类 2替换区域 3替换排序
	 * @param catePath
	 * @param regionPath
	 * @param sort
	 * @return
	 */
	public static String replaceStorePageInfo(StorePageInfo info, int type, String catePath, String regionPath, String sort, String queryString) {
		StringBuffer sb = new StringBuffer("store");
		if (type == 1) {
			if (StringUtils.isBlank(info.getCatePre()) || "all".equals(info.getCatePre())) {
				sb.append("/").append(catePath);
			} else {
				sb.append("/").append(info.getCatePre()).append("-").append(catePath);
			}
			if (StringUtils.isNotBlank(info.getReginPath())) {
				sb.append("/").append(info.getReginPath());
			}

			if (StringUtils.isNotBlank(info.getSortPath())) {
				sb.append("/").append(info.getSortPath());
			}
		} else if (type == 2) {
			if (StringUtils.isBlank(info.getCatePre())) {
				sb.append("/all");
			} else {
				sb.append("/").append(info.getCatePre());
				if (StringUtils.isNotBlank(info.getCateLatter())) {
					sb.append("-").append(info.getCateLatter());
				}
			}
			sb.append("/").append(regionPath);
			if (StringUtils.isNotBlank(info.getSortPath())) {
				sb.append("/").append(info.getSortPath());
			}
		} else if (type == 3) {
			if (StringUtils.isBlank(info.getCatePre())) {
				sb.append("/all");
			} else {
				sb.append("/").append(info.getCatePre());
				if (StringUtils.isNotBlank(info.getCateLatter())) {
					sb.append("-").append(info.getCateLatter());
				}
			}
			if (StringUtils.isNotBlank(info.getReginPath())) {
				sb.append("/").append(info.getReginPath());
			} else {
				sb.append("/all");
			}
			sb.append("/").append(sort);
		}
		if (StringUtils.isNotEmpty(info.getPagePath())) {
			sb.append("/").append(info.getPagePath());
		}
		return sb.append(".jhtml").toString();
	}

	public static void main(String[] args) {
		int pageNo = URLHelper.getPageNo("/shops/all/all/sales/page2.jhtml");
		System.out.println(pageNo);

		PageInfo info = URLHelper.getPageInfo("/shops/all/all/sales/page2.jhtml", null);
		System.out.println(pageNo);

		StorePageInfo storeInfo = URLHelper.getStorePageInfo("/store/food.jhtml", null);
		String url2 = URLHelper.replaceStorePageInfo(storeInfo, 1, "huoguo", null, null, null);
		String url1 = URLHelper.replaceStorePageInfo(storeInfo, 2, null, "ouhaiqu", null, null);
		System.out.println(url1);
	}

	/**
	 * URI信息
	 */
	public static class PageInfo {
		/**
		 * 页面地址
		 */
		private String href;
		/**
		 * href前半部（相对于分页）
		 */
		private String hrefFormer;
		/**
		 * href后半部（相对于分页）
		 */
		private String hrefLatter;

		public PageInfo(String href, String hrefFormer, String hrefLatter) {
			this.href = href;
			this.hrefFormer = hrefFormer;
			this.hrefLatter = hrefLatter;
		}

		public String getHref() {
			return href;
		}

		public void setHref(String href) {
			this.href = href;
		}

		public String getHrefFormer() {
			return hrefFormer;
		}

		public void setHrefFormer(String hrefFormer) {
			this.hrefFormer = hrefFormer;
		}

		public String getHrefLatter() {
			return hrefLatter;
		}

		public void setHrefLatter(String hrefLatter) {
			this.hrefLatter = hrefLatter;
		}

	}

	/**
	 * URI信息
	 */
	public static class StorePageInfo {
		/**
		 * 主分类路径
		 */
		private String catePre;
		/**
		 * 子分类路径
		 */
		private String cateLatter;
		/**
		 * 区域路径
		 */
		private String reginPath;
		/**
		 * 排序路径
		 */
		private String sortPath;

		private String pagePath;

		private int len;

		public StorePageInfo(String catePre, String cateLatter, String reginPath, String sortPath, String pagePath, int len) {
			this.catePre = catePre;
			this.cateLatter = cateLatter;
			this.reginPath = reginPath;
			this.sortPath = sortPath;
			this.pagePath = pagePath;
			this.len = len;
		}

		// public String toUrl(){
		// StringBuffer sb = new StringBuffer();
		// sb.append("/").append(this.getCatePre());
		// if(StringUtils.isNotBlank(this.getCateLatter())) {
		// sb.append("-").append(this.getCateLatter())
		// }
		// return null;
		// }

		public StorePageInfo() {

		}

		public String getCatePre() {
			return catePre;
		}

		public void setCatePre(String catePre) {
			this.catePre = catePre;
		}

		public String getCateLatter() {
			return cateLatter;
		}

		public void setCateLatter(String cateLatter) {
			this.cateLatter = cateLatter;
		}

		public String getReginPath() {
			return reginPath;
		}

		public void setReginPath(String reginPath) {
			this.reginPath = reginPath;
		}

		public String getSortPath() {
			return sortPath;
		}

		public void setSortPath(String sortPath) {
			this.sortPath = sortPath;
		}

		public String getPagePath() {
			return pagePath;
		}

		public void setPagePath(String pagePath) {
			this.pagePath = pagePath;
		}

	}
}
