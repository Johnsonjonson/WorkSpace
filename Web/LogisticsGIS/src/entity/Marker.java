package entity;

public class Marker {
    private int id;
    private String name;
    private double lng;
    private double lat;
    private String title;
    private String htmlData;
    private Double transferTime;
    private Double transferFeeRate;
    private String drawingType;
    private int flag;
    private String subType;
    private String type;
    private String imgName;

    public void setTransferTime(Double transferTime) {
        this.transferTime = transferTime;
    }

    public void setTransferFeeRate(Double transferFeeRate) {
        this.transferFeeRate = transferFeeRate;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHtmlData() {
        return htmlData;
    }

    public void setHtmlData(String htmlData) {
        this.htmlData = htmlData;
    }

    public double getTransferTime() {
        return transferTime;
    }

    public void setTransferTime(double transferTime) {
        this.transferTime = transferTime;
    }

    public double getTransferFeeRate() {
        return transferFeeRate;
    }

    public void setTransferFeeRate(double transferFeeRate) {
        this.transferFeeRate = transferFeeRate;
    }

    public String getDrawingType() {
        return drawingType;
    }

    public void setDrawingType(String drawingType) {
        this.drawingType = drawingType;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Marker{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lng=" + lng +
                ", lat=" + lat +
                ", title='" + title + '\'' +
                ", htmlData='" + htmlData + '\'' +
                ", transferTime=" + transferTime +
                ", transferFeeRate=" + transferFeeRate +
                ", drawingType='" + drawingType + '\'' +
                ", flag=" + flag +
                ", subType='" + subType + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
