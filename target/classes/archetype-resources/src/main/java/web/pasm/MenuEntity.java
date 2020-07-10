package ${package}.web.pasm;

import java.util.List;

public class MenuEntity implements Cloneable{
	int sort;
	String id;
	String text;
	String href;
	String target;
	boolean visible;
	String imgClass;
	List<MenuEntity> childen;

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public List<MenuEntity> getChilden() {
		return childen;
	}

	public String getImgClass() {
		return imgClass;
	}

	public void setImgClass(String imgClass) {
		this.imgClass = imgClass;
	}

	public void setChilden(List<MenuEntity> childen) {
		this.childen = childen;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		
		MenuEntity menu = null;
	        try{  
	        	menu = (MenuEntity)super.clone();
	        }catch(CloneNotSupportedException e) {  
	            e.printStackTrace();  
	        }    
	        return menu;  
	}
	
}
