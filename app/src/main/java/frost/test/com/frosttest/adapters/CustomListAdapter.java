package frost.test.com.frosttest.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import frost.test.com.frosttest.R;
import frost.test.com.frosttest.model.CategoryListItemElement;

public class CustomListAdapter  extends BaseAdapter {
    private ArrayList<CategoryListItemElement> elementsData;
    private ArrayList<CategoryListItemElement> elements;
    private LayoutInflater inflater;
    private Context context;
    Drawable upArrowDrawable,downArrowDrawable,levelOneIc,levelTwoIc,levelThreeIc;
    private ArrayList<String> selectedCategories;
    int indentionBase = 30;


    public CustomListAdapter(Context context, ArrayList<CategoryListItemElement> elements, ArrayList<CategoryListItemElement> elementsData,
                             LayoutInflater inflater) {
        // TODO Auto-generated constructor stub
        this.context=context;
        this.elements = elements;
        this.elementsData = elementsData;
        this.inflater = inflater;

        upArrowDrawable=ResourcesCompat.getDrawable(context.getResources(),R.drawable.arrow_up,null);
        downArrowDrawable=ResourcesCompat.getDrawable(context.getResources(),R.drawable.arrow_down,null);

        levelOneIc=ResourcesCompat.getDrawable(context.getResources(),R.drawable.folder_ic,null);
        levelTwoIc=ResourcesCompat.getDrawable(context.getResources(),R.drawable.sub_folder_ic,null);
        levelThreeIc=ResourcesCompat.getDrawable(context.getResources(),R.drawable.sub_sub_folder_ic,null);

    }

    public ArrayList<CategoryListItemElement> getElements() {
        return elements;
    }

    public ArrayList<CategoryListItemElement> getElementsData() {
        return elementsData;
    }

    @Override
    public int getCount() {
        return elements.size();
    }

    @Override
    public Object getItem(int position) {
        return elements.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_items, null);
            holder.disclosureImg = (ImageView) convertView.findViewById(R.id.dataItemArrow);
            holder.logoImg = (ImageView) convertView.findViewById(R.id.logo);
            holder.contentText =  convertView.findViewById(R.id.dataItemName);
            holder.layout=convertView.findViewById(R.id.layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        final CategoryListItemElement element = elements.get(position);
        int level = element.getLevel();
        holder.layout.setPadding( indentionBase * (level + 1),
                60,
                50,
                60);
        holder.contentText.setText(element.getContentText());
        if (element.isHasChildren() && !element.isExpanded()) {
            holder.disclosureImg.setVisibility(View.VISIBLE);
            holder.disclosureImg.setImageDrawable(downArrowDrawable);
        } else if (element.isHasChildren() && element.isExpanded()) {
            holder.disclosureImg.setVisibility(View.VISIBLE);
            holder.disclosureImg.setImageDrawable(upArrowDrawable);
        } else if (!element.isHasChildren()) {
            holder.disclosureImg.setVisibility(View.GONE);
        }

        holder.logoImg.setImageDrawable(getRespectiveIcon(element.getLevel()));

        return convertView;
    }


    public void setSelectedArray(ArrayList<String> selectedArray)
    {
        selectedCategories=selectedArray;
        notifyDataSetChanged();
    }


    static class ViewHolder{
        ImageView disclosureImg, logoImg;
        TextView contentText;
        RelativeLayout layout;
    }

    public Drawable getRespectiveIcon(int level)
    {
        if(level==0)
            return levelOneIc;
        else if(level==1)
            return levelTwoIc;
        else
            return levelThreeIc;
    }
}

