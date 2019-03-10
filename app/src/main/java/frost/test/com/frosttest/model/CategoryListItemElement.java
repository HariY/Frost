package frost.test.com.frosttest.model;

import android.support.annotation.NonNull;

public class CategoryListItemElement implements Comparable<CategoryListItemElement>{

    private String contentText;

    private int level;

    private int id;

    private int parendId;

    private boolean hasChildren;

    private boolean isExpanded;

    private boolean isSelected;

    private String fullName;

    private String visibleCategoryName;

    private int countValue;

    public static final int NO_PARENT = -1;

    public static final int TOP_LEVEL = 0;

    public CategoryListItemElement(String contentText, int level, int id, int parendId,
                                   boolean hasChildren, boolean isExpanded,String fullName,boolean isSelected,int count,String visibleCategoryName) {
        super();
        this.level = level;
        this.id = id;
        this.parendId = parendId;
        this.hasChildren = hasChildren;
        this.isExpanded = isExpanded;
        this.fullName = fullName;
        this.isSelected=isSelected;
        this.countValue=count;
        if(visibleCategoryName!=null && visibleCategoryName.length()>0)
            this.contentText=visibleCategoryName;
        else
            this.contentText = contentText;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean isExpanded) {
        this.isExpanded = isExpanded;
    }

    public String getContentText() {
        return contentText;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParendId() {
        return parendId;
    }

    public void setParendId(int parendId) {
        this.parendId = parendId;
    }

    public boolean isHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public int getCountValue() {
        return countValue;
    }

    public void setCountValue(int countValue) {
        this.countValue = countValue;
    }



    @Override
    public int compareTo(@NonNull CategoryListItemElement o) {
        return this.getContentText().compareTo(o.getContentText()); // dog name sort in ascending order
        //return o.getName().compareTo(this.name); use this line for dog name sort in descending order
    }
}