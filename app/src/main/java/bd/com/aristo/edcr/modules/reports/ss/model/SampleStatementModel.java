package bd.com.aristo.edcr.modules.reports.ss.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.annotations.SerializedName;
import com.mikepenz.fastadapter.IItem;

import java.util.Collections;
import java.util.List;

import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.utils.StringUtils;
import bd.com.aristo.edcr.utils.SystemUtils;
import bd.com.aristo.edcr.utils.constants.StringConstants;
import bd.com.aristo.edcr.utils.ui.texts.ATextView;
import io.realm.annotations.Ignore;

/**
 * Created by monir.sobuj on 01/11/17.
 */

public class SampleStatementModel implements IItem<SampleStatementModel, SampleStatementModel.ViewHolder> {

    @SerializedName("CentralQty")
    private int centralQty;
    @SerializedName("ClosingStock")
    private int closingStock;
    @SerializedName("GivenQty0108")
    private int givenFirst;
    @SerializedName("GivenQty0915")
    private int givenSecond;
    @SerializedName("GivenQty1623")
    private int givenThird;
    @SerializedName("GivenQty2431")
    private int givenFourth;
    @SerializedName("RestQty")
    private int carryQty;
    @SerializedName("TotalDCR0108")
    private int executeFirst;
    @SerializedName("TotalDCR0915")
    private int executeSecond;
    @SerializedName("TotalDCR1623")
    private int executeThird;
    @SerializedName("TotalDCR2431")
    private int executeFourth;
    @SerializedName("TotalQty")
    private int totalQty;
    @SerializedName("TotalQtyAddDefi0130")
    private int totalAddOrSub;
    @SerializedName("TotalStock0108")
    private int stockFirst;
    @SerializedName("TotalStock0915")
    private int stockSecond;
    @SerializedName("TotalStock1623")
    private int stockThird;
    @SerializedName("TotalStock2431")
    private int stockFourth;

    @SerializedName("d01")
    private int day01;
    @SerializedName("d02")
    private int day02;
    @SerializedName("d03")
    private int day03;
    @SerializedName("d04")
    private int day04;
    @SerializedName("d05")
    private int day05;
    @SerializedName("d06")
    private int day06;
    @SerializedName("d07")
    private int day07;
    @SerializedName("d08")
    private int day08;
    @SerializedName("d09")
    private int day09;
    @SerializedName("d10")
    private int day10;
    @SerializedName("d11")
    private int day11;
    @SerializedName("d12")
    private int day12;
    @SerializedName("d13")
    private int day13;
    @SerializedName("d14")
    private int day14;
    @SerializedName("d15")
    private int day15;
    @SerializedName("d16")
    private int day16;
    @SerializedName("d17")
    private int day17;
    @SerializedName("d18")
    private int day18;
    @SerializedName("d19")
    private int day19;
    @SerializedName("d20")
    private int day20;
    @SerializedName("d21")
    private int day21;
    @SerializedName("d22")
    private int day22;
    @SerializedName("d23")
    private int day23;
    @SerializedName("d24")
    private int day24;
    @SerializedName("d25")
    private int day25;
    @SerializedName("d26")
    private int day26;
    @SerializedName("d27")
    private int day27;
    @SerializedName("d28")
    private int day28;
    @SerializedName("d29")
    private int day29;
    @SerializedName("d30")
    private int day30;
    @SerializedName("d31")
    private int day31;

    @SerializedName("ItemType")
    private String itemType;
    @SerializedName("MPGroup")
    private String mpoGroup;
    @SerializedName("MarketName")
    private String marketName;
    @SerializedName("ProductCode")
    private String productCode;
    @SerializedName("ProductName")
    private String productName;
    @SerializedName("ItemFor")
    private String itemFor; //R, I

    private int totalExecution;

    @Ignore
    protected boolean isSelected = false; // defines if the item is selected

    @Ignore
    protected Object tag;// defines if this item is isSelectable

    @Ignore
    protected boolean isSelectable = true;

    public SampleStatementModel() {
    }

    public int getCentralQty() {
        return centralQty;
    }

    public void setCentralQty(int centralQty) {
        this.centralQty = centralQty;
    }

    public int getClosingStock() {
        return closingStock;
    }

    public void setClosingStock(int closingStock) {
        this.closingStock = closingStock;
    }

    public int getGivenFirst() {
        return givenFirst;
    }

    public void setGivenFirst(int givenFirst) {
        this.givenFirst = givenFirst;
    }

    public int getGivenSecond() {
        return givenSecond;
    }

    public void setGivenSecond(int givenSecond) {
        this.givenSecond = givenSecond;
    }

    public int getGivenThird() {
        return givenThird;
    }

    public void setGivenThird(int givenThird) {
        this.givenThird = givenThird;
    }

    public int getGivenFourth() {
        return givenFourth;
    }

    public void setGivenFourth(int givenFourth) {
        this.givenFourth = givenFourth;
    }

    public int getCarryQty() {
        return carryQty;
    }

    public void setCarryQty(int carryQty) {
        this.carryQty = carryQty;
    }

    public int getExecuteFirst() {
        return executeFirst;
    }

    public void setExecuteFirst(int executeFirst) {
        this.executeFirst = executeFirst;
    }

    public int getExecuteSecond() {
        return executeSecond;
    }

    public void setExecuteSecond(int executeSecond) {
        this.executeSecond = executeSecond;
    }

    public int getExecuteThird() {
        return executeThird;
    }

    public void setExecuteThird(int executeThird) {
        this.executeThird = executeThird;
    }

    public int getExecuteFourth() {
        return executeFourth;
    }

    public void setExecuteFourth(int executeFourth) {
        this.executeFourth = executeFourth;
    }

    public int getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(int totalQty) {
        this.totalQty = totalQty;
    }

    public int getTotalAddOrSub() {
        return totalAddOrSub;
    }

    public void setTotalAddOrSub(int totalAddOrSub) {
        this.totalAddOrSub = totalAddOrSub;
    }

    public int getStockFirst() {
        return stockFirst;
    }

    public void setStockFirst(int stockFirst) {
        this.stockFirst = stockFirst;
    }

    public int getStockSecond() {
        return stockSecond;
    }

    public void setStockSecond(int stockSecond) {
        this.stockSecond = stockSecond;
    }

    public int getStockThird() {
        return stockThird;
    }

    public void setStockThird(int stockThird) {
        this.stockThird = stockThird;
    }

    public int getStockFourth() {
        return stockFourth;
    }

    public void setStockFourth(int stockFourth) {
        this.stockFourth = stockFourth;
    }

    public int getDay01() {
        return day01;
    }

    public void setDay01(int day01) {
        this.day01 = day01;
    }

    public int getDay02() {
        return day02;
    }

    public void setDay02(int day02) {
        this.day02 = day02;
    }

    public int getDay03() {
        return day03;
    }

    public void setDay03(int day03) {
        this.day03 = day03;
    }

    public int getDay04() {
        return day04;
    }

    public void setDay04(int day04) {
        this.day04 = day04;
    }

    public int getDay05() {
        return day05;
    }

    public void setDay05(int day05) {
        this.day05 = day05;
    }

    public int getDay06() {
        return day06;
    }

    public void setDay06(int day06) {
        this.day06 = day06;
    }

    public int getDay07() {
        return day07;
    }

    public void setDay07(int day07) {
        this.day07 = day07;
    }

    public int getDay08() {
        return day08;
    }

    public void setDay08(int day08) {
        this.day08 = day08;
    }

    public int getDay09() {
        return day09;
    }

    public void setDay09(int day09) {
        this.day09 = day09;
    }

    public int getDay10() {
        return day10;
    }

    public void setDay10(int day10) {
        this.day10 = day10;
    }

    public int getDay11() {
        return day11;
    }

    public void setDay11(int day11) {
        this.day11 = day11;
    }

    public int getDay12() {
        return day12;
    }

    public void setDay12(int day12) {
        this.day12 = day12;
    }

    public int getDay13() {
        return day13;
    }

    public void setDay13(int day13) {
        this.day13 = day13;
    }

    public int getDay14() {
        return day14;
    }

    public void setDay14(int day14) {
        this.day14 = day14;
    }

    public int getDay15() {
        return day15;
    }

    public void setDay15(int day15) {
        this.day15 = day15;
    }

    public int getDay16() {
        return day16;
    }

    public void setDay16(int day16) {
        this.day16 = day16;
    }

    public int getDay17() {
        return day17;
    }

    public void setDay17(int day17) {
        this.day17 = day17;
    }

    public int getDay18() {
        return day18;
    }

    public void setDay18(int day18) {
        this.day18 = day18;
    }

    public int getDay19() {
        return day19;
    }

    public void setDay19(int day19) {
        this.day19 = day19;
    }

    public int getDay20() {
        return day20;
    }

    public void setDay20(int day20) {
        this.day20 = day20;
    }

    public int getDay21() {
        return day21;
    }

    public void setDay21(int day21) {
        this.day21 = day21;
    }

    public int getDay22() {
        return day22;
    }

    public void setDay22(int day22) {
        this.day22 = day22;
    }

    public int getDay23() {
        return day23;
    }

    public void setDay23(int day23) {
        this.day23 = day23;
    }

    public int getDay24() {
        return day24;
    }

    public void setDay24(int day24) {
        this.day24 = day24;
    }

    public int getDay25() {
        return day25;
    }

    public void setDay25(int day25) {
        this.day25 = day25;
    }

    public int getDay26() {
        return day26;
    }

    public void setDay26(int day26) {
        this.day26 = day26;
    }

    public int getDay27() {
        return day27;
    }

    public void setDay27(int day27) {
        this.day27 = day27;
    }

    public int getDay28() {
        return day28;
    }

    public void setDay28(int day28) {
        this.day28 = day28;
    }

    public int getDay29() {
        return day29;
    }

    public void setDay29(int day29) {
        this.day29 = day29;
    }

    public int getDay30() {
        return day30;
    }

    public void setDay30(int day30) {
        this.day30 = day30;
    }

    public int getDay31() {
        return day31;
    }

    public void setDay31(int day31) {
        this.day31 = day31;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getMpoGroup() {
        return mpoGroup;
    }

    public void setMpoGroup(String mpoGroup) {
        this.mpoGroup = mpoGroup;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getItemFor() {
        return itemFor;
    }

    public void setItemFor(String itemFor) {
        this.itemFor = itemFor;
    }

    public int getTotalExecution() {
        return totalExecution;
    }

    public void setTotalExecution(int totalExecution) {
        this.totalExecution = totalExecution;
    }

    @Override
    public Object getTag() {
        return tag;
    }

    @Override
    public SampleStatementModel withTag(Object tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public SampleStatementModel withEnabled(boolean enabled) {
        return null;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public SampleStatementModel withSetSelected(boolean selected) {
        return null;
    }

    @Override
    public boolean isSelectable() {
        return isSelectable;
    }

    @Override
    public SampleStatementModel withSelectable(boolean selectable) {
        this.isSelectable = selectable;
        return this;
    }


    @Override
    public int getType() {
        return R.id.mRecyclerView;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_sample_statement;
    }

    @Override
    public View generateView(Context ctx) {
        SampleStatementModel.ViewHolder viewHolder = getViewHolder(LayoutInflater.from(ctx).inflate(getLayoutRes(), null, false));
        bindView(viewHolder, Collections.EMPTY_LIST);
        return viewHolder.itemView;
    }

    @Override
    public View generateView(Context ctx, ViewGroup parent) {
        SampleStatementModel.ViewHolder viewHolder = getViewHolder(LayoutInflater.from(ctx).inflate(getLayoutRes(), parent, false));
        bindView(viewHolder, Collections.EMPTY_LIST);
        return viewHolder.itemView;
    }


    private SampleStatementModel.ViewHolder getViewHolder(View view) {
        return new SampleStatementModel.ViewHolder(view);
    }

    @Override
    public SampleStatementModel.ViewHolder getViewHolder(ViewGroup parent) {
        return getViewHolder(LayoutInflater.from(parent.getContext()).inflate(getLayoutRes(), parent, false));
    }

    @Override
    public void bindView(SampleStatementModel.ViewHolder holder, List<Object> payloads) {
        Context ctx = holder.itemView.getContext();
        int totalExecution = totalQty - closingStock;
        setTotalExecution(totalExecution);
        holder.txtName.setText(productName);
        holder.txtAllocation.setText(String.valueOf(centralQty));
        holder.txtAddDef.setText(String.valueOf(totalAddOrSub));
        holder.txtCarry.setText(String.valueOf(carryQty));
        holder.txtTotal.setText(String.valueOf(totalQty));
        holder.txtFirst.setText(String.valueOf(executeFirst));
        holder.txtSecond.setText(String.valueOf(executeSecond));
        holder.txtThird.setText(String.valueOf(executeThird));
        holder.txtFourth.setText(String.valueOf(executeFourth));
        holder.txtExecute.setText(String.valueOf(totalExecution));
        holder.txtBalance.setText(String.valueOf(closingStock));

        if (itemFor!=null
                && itemFor.equalsIgnoreCase(StringConstants.ITEM_FOR_INTERN)
                && itemType != null
                && itemType.equalsIgnoreCase(StringConstants.GIFT_ITEM)) {
            holder.llMain.setBackgroundColor(SystemUtils.getColorFromID(ctx, R.color.light_gray));

        }
    }

    @Override
    public void unbindView(SampleStatementModel.ViewHolder holder) {
        holder.txtName.setText(null);
        holder.txtAllocation.setText(null);
        holder.txtAddDef.setText(null);
        holder.txtCarry.setText(null);
        holder.txtTotal.setText(null);
        holder.txtFirst.setText(null);
        holder.txtSecond.setText(null);
        holder.txtThird.setText(null);
        holder.txtFourth.setText(null);
        holder.txtExecute.setText(null);
        holder.txtBalance.setText(null);
    }

    @Override
    public boolean equals(int id) {
        return false;
    }

    @Override
    public SampleStatementModel withIdentifier(long identifier) {
        return this;
    }

    @Override
    public long getIdentifier() {
        return StringUtils.hashString64Bit(productCode);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        ATextView txtName, txtAllocation, txtAddDef, txtCarry, txtTotal, txtFirst, txtSecond, txtThird, txtFourth, txtExecute, txtBalance;
        LinearLayout llMain;
        public ViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtAllocation = itemView.findViewById(R.id.txtAllocation);
            txtAddDef = itemView.findViewById(R.id.txtAddDef);
            txtCarry = itemView.findViewById(R.id.txtCarry);
            txtTotal = itemView.findViewById(R.id.txtTotal);
            txtFirst = itemView.findViewById(R.id.txtFirst);
            txtSecond = itemView.findViewById(R.id.txtSecond);
            txtThird = itemView.findViewById(R.id.txtThird);
            txtFourth = itemView.findViewById(R.id.txtFourth);
            txtExecute = itemView.findViewById(R.id.txtExecute);
            txtBalance = itemView.findViewById(R.id.txtBalance);
            llMain = itemView.findViewById(R.id.llMain);
        }
    }
}
