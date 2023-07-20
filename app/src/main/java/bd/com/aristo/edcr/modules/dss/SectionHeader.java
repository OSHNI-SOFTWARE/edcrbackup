package bd.com.aristo.edcr.modules.dss;

import com.intrusoft.sectionedrecyclerview.Section;

import java.util.List;

/**
 * Created by altaf.sil on 1/23/18.
 */

public class SectionHeader implements Section<DSSModel> {

    List<DSSModel> childList;
    String sectionText;//sample, selected ,gift item

    public SectionHeader(List<DSSModel> childList, String sectionText) {
        this.childList = childList;
        this.sectionText = sectionText;
    }

    @Override
    public List<DSSModel> getChildItems() {
        return childList;
    }

    public String getSectionText() {
        return sectionText;
    }

}
