package main;

public class ConvertTimeLine {
    String timeline;

    public ConvertTimeLine(String timeline){
        this.timeline = timeline;
        convert();
    }

    private void convert(){ // convert Alphabet to Hangul
        String[] splitTime = timeline.split("/");
        for (int i = 0 ; i < splitTime.length; i++){
            if (splitTime[i].contains("M"))
                splitTime[i] = splitTime[i].replace("M", "월");
            else if (splitTime[i].contains("TU"))
                splitTime[i] = splitTime[i].replace("TU", "화");
            else if (splitTime[i].contains("W"))
                splitTime[i] = splitTime[i].replace("W", "수");
            else if (splitTime[i].contains("T"))
                splitTime[i] = splitTime[i].replace("T", "목");
            else if (splitTime[i].contains("F"))
                splitTime[i] = splitTime[i].replace("F", "금");
        }

        String result = "";
        for (int i = 0 ; i < splitTime.length-1; i++)
            result += splitTime[i] + "/";
        result += splitTime[splitTime.length-1];
        timeline = result;
    }

    public String getTimeLine(){
        return timeline;
    }
}
