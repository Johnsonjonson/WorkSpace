package entity;

public class Polyline {
    private int id;
    private String name;
    private double lng;
    private double lat;
    private String title;
    private String htmlData;
    private double transferDistance;
    private int transferTime;
    private int transferFeeRate;
    private String drawingType;
    private int flag;
    private String subType;
    private String type;
    private String coordinates;
    private int endSiteId;
    private int startSiteId;

    public double getTransferDistance() {
        return transferDistance;
    }

    public void setTransferDistance(double transferDistance) {
        this.transferDistance = transferDistance;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public int getEndSiteId() {
        return endSiteId;
    }

    public void setEndSiteId(int endSiteId) {
        this.endSiteId = endSiteId;
    }

    public int getStartSiteId() {
        return startSiteId;
    }

    public void setStartSiteId(int startSiteId) {
        this.startSiteId = startSiteId;
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

    public int getTransferTime() {
        return transferTime;
    }

    public void setTransferTime(int transferTime) {
        this.transferTime = transferTime;
    }

    public int getTransferFeeRate() {
        return transferFeeRate;
    }

    public void setTransferFeeRate(int transferFeeRate) {
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
}
