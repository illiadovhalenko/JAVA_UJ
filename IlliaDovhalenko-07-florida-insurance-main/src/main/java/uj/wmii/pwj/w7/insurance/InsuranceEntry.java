package uj.wmii.pwj.w7.insurance;
public class InsuranceEntry{
    String policyID;
    String stateCode;
    String county;
    double eq_site_limit;
    double hu_site_limit;
    double fl_site_limit;
    double fr_site_limit;
    double tiv_2011;
    double tiv_2012;
    double eq_site_deductible;
    double hu_site_deductible;
    double fl_site_deductible;
    double fr_site_deductible;
    String point_latitude;
    String point_longitude;
    String line;
    String construction;
    String point_granularity;
    public InsuranceEntry(String input){
        String[] data = input.split(",");
        policyID = data[0];
        stateCode = data[1];
        county = data[2];
        eq_site_limit = Double.parseDouble(data[3]);
        hu_site_limit = Double.parseDouble(data[4]);
        fl_site_limit = Double.parseDouble(data[5]);
        fr_site_limit = Double.parseDouble(data[6]);
        tiv_2011 = Double.parseDouble(data[7]);
        tiv_2012 = Double.parseDouble(data[8]);
        eq_site_deductible = Double.parseDouble(data[9]);
        hu_site_deductible = Double.parseDouble(data[10]);
        fl_site_deductible = Double.parseDouble(data[11]);
        fr_site_deductible = Double.parseDouble(data[12]);
        point_latitude = data[13];
        point_longitude = data[14];
        line = data[15];
        construction = data[16];
        point_granularity = data[17];
    }
    public String getCounty(){
        return county;
    }
    public double getTiv_2012(){
        return tiv_2012;
    }
    public double getTiv_2011(){
        return tiv_2011;
    }
}
