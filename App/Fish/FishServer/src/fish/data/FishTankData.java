package fish.data;

public class FishTankData {
    public static boolean needChange = false;  // 是否需要换水
    public static double curTemp;  // 当前温度

    public FishTankData(Boolean needChange,Double curTemp) {
        this.needChange = needChange;
        this.curTemp = curTemp;
    }

    public static boolean getNeedChange() {
        return needChange;
    }

    public static void setNeedChange(Boolean needChange) {
        FishTankData.needChange = needChange;
    }

    public static void setCurTemp(Double curTemp) {
        FishTankData.curTemp = curTemp;
    }

    public static double getCurTemp() {
        return curTemp;
    }
}
