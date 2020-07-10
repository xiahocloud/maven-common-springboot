package ${package}.web.pasm;


import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MenuManager {

	private static Logger logger = LoggerFactory.getLogger(MenuManager.class);
	private static List<MenuEntity> menuConfig = null;
	private static Map<String, String> mapUrl = null;
	private static Lock lock = new ReentrantLock();
	public static MenuManager Instance;


	static {
		try {
			lock.lock();
			if (Instance == null) {
				Instance = new MenuManager();
			}
		} catch (Exception ex) {
			logger.error("error:{}", ex);
		} finally {
			lock.unlock();
		}
	}

	private MenuManager() {

	}

	public boolean reload() {
		try {
			loadMenuCofing();
			return true;
		} catch (Exception e) {
			logger.error("framework.error", e);
		}
		return false;
	}


	public List<MenuEntity> getMenuList() {
		try {
			if (menuConfig == null || menuConfig.size() == 0) {
				loadMenuCofing();
			}
		} catch (Exception e) {
			logger.error("framework.error", e);
		}

		return menuConfig;
	}

	public Map<String, String> getMapUrl() {
		if (mapUrl == null) {
			reload();
		}
		return mapUrl;
	}

	private Element getXmlNode(List<Element> nodes, String id) {
		Element node = null;
		for (Element ele : nodes) {
			List<Element> chileds = ele.selectNodes("item");
			if (ele.attributeValue("id").equals(id)) {
				node = ele;
			} else if (chileds != null && chileds.size() > 0) {
				node = getXmlNode(chileds, id);
			}

			if (node != null) {
				break;
			}
		}
		return node;
	}

	@SuppressWarnings("deprecation")
	private Document getXmlDocument() throws Exception {
		Map<String, String> xmlConfig = ResourceUtils.getFiles("", ".xml");
		SAXReader reader = new SAXReader();
		InputStream in = new ByteArrayInputStream(xmlConfig.get("menu.xml").getBytes("UTF-8"));
		Document doc = reader.read(in);
		return doc;
	}

	private void loadMenuCofing() throws Exception {
		List roots = getXmlDocument().selectNodes("config/item");
		List<MenuEntity> newmenuConfig = new ArrayList<>();
		Map<String, String> mapUrl = new HashMap<>();
		readXml(roots, newmenuConfig, mapUrl);

		menuConfig = newmenuConfig;

		MenuManager.mapUrl = mapUrl;
		sortMenuConfig(menuConfig);
	}

	private boolean hasMenuConfig(List<MenuEntity> menuConfig, String id) {
		boolean retObj = false;
		for (MenuEntity config : menuConfig) {
			if (config.getId().equals(id)) {
				retObj = true;
			} else if (config.getChilden() != null && config.getChilden().size() > 0) {
				retObj = hasMenuConfig(config.getChilden(), id);
			}

			if (retObj) {
				break;
			}
		}

		return retObj;
	}

	private List<MenuEntity> findMenuConfig(List<MenuEntity> menuConfig, String pid) {
		List<MenuEntity> retObj = new ArrayList<>();
		for (MenuEntity config : menuConfig) {
			if (config.getId().equals(pid)) {
				retObj = config.getChilden();
				break;
			} else if (config.getChilden() != null && config.getChilden().size() > 0) {
				retObj = findMenuConfig(config.getChilden(), pid);
				if (retObj != null && retObj.size() > 0) {
					break;
				}
			}
		}

		return retObj;
	}

	private void sortMenuConfig(List<MenuEntity> menus) {
		for (int i = 0; i < menus.size(); i++) {
			for (int x = i + 1; x < menus.size(); x++) {
				if (menus.get(i).getSort() > menus.get(x).getSort()) {
					MenuEntity p = menus.get(i);
					menus.set(i, menus.get(x));
					menus.set(x, p);
				}
			}
			List<MenuEntity> childs = menus.get(i).getChilden();
			if (childs != null && childs.size() > 0) {
				sortMenuConfig(childs);
			}
		}
	}

	private void readXml(List<Element> nodes, List<MenuEntity> configs, Map<String, String> mapUrl) {
		for (Element node : nodes) {
			try {
				MenuEntity entity = new MenuEntity();
				entity.setId(node.attributeValue("id"));
				entity.setText(node.attributeValue("text"));
				if (node.attributeValue("href") != null) {
					entity.setHref(node.attributeValue("href"));
					mapUrl.put(entity.getId(), entity.getHref());
				}
				entity.setTarget(node.attributeValue("target"));
				entity.setVisible("true".equals(node.attributeValue("visible")));
				entity.setSort(Integer.parseInt(node.attributeValue("sort")));

				if (node.attributeValue("imgclass") != null) {
					entity.setImgClass(node.attributeValue("imgclass"));
				}

				List<Element> chileds = node.selectNodes("item");
				if (chileds != null && chileds.size() > 0) {
					entity.setChilden(new ArrayList<>());
					readXml(chileds, entity.getChilden(), mapUrl);
				}

				configs.add(entity);
			} catch (Exception ex) {
				logger.error("framework.error", ex);
			}
		}
	}


}
