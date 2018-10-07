package pipe.hla.SimDSPNModule.ordinarysimulation.ordinarymodel;

public class PlaceToken{
    //每个库所的名字
    private String placeName;
    //每个库所中token的数量
    private int tokenNum;

    public PlaceToken(String placeName, int tokenNum) {
        this.placeName = placeName;
        this.tokenNum = tokenNum;
    }

    @Override
    public String toString() {
        return " {" + placeName + " : " + tokenNum + "} ";
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public int getTokenNum() {
        return tokenNum;
    }

    public void setTokenNum(int tokenNum) {
        this.tokenNum = tokenNum;
    }
}
