package icom5047.aerobal.resources;

public class Keys {

    //Class to Store all the Shared Preferences Key
    public static class SharedPref {

        public static String UserSharedPreferences = "userSharedPref";
        public static String LoginStatus = "loginStatus";
        public static String SessionId = "sessionId";
        public static String ExperimentId = "experimentId";


    }

    public static class CallbackMap {
        public static String BluetoothDevice = "btDevice";
        public static String BluetoothDialog = "btDialog";
        public static String BluetoothConnectedStatus = "btConneectedStatus";
        public static String UnitsDialog = "unitsDialog";
        public static String UnitsBtnType = "unitBtnType";
        public static String UnitsDefault = "unitsDefault";
        public static String UnitsSaveNew = "unitsSave";
        public static String UnitsPressure = "unitsPressure";
        public static String UnitsForce = "unitsForce";
        public static String UnitsHumidity = "unitsHumidity";
        public static String UnitsTemperature = "unitsTemperature";
        public static String UnitsSpeed = "unitsSpeed";
        public static String UnitsDirection = "unitsDirection";
        public static String NewExperimentDialog = "newExperimentDialog";
        public static String NewExperimentObject = "newExperimentObject";
        public static String OpenType = "openType";

    }


    public static class FragmentTag {
        public static String EmptyTag = "emptyFragmentTag";
        public static String ExperimentTag = "experimentFragmentTag";
        public static String UpdateTag = "updateFragmentTag";
    }

    public static class BundleKeys{

        public static String ExperimentController = "keyExperimentController";
        public static String UnitController = "keyUnitController";
        public static String OpenType = "openType";
        public static String XAxis = "xAxis";
        public static String YAxis = "yAxis";
        public static String XAxisTitle = "xAxisTitle";
        public static String YAxisTitle = "yAxisTitle";
        public static String UserToken = "userToken";
        public static String UserController = "keyUserController";
        public static String FileRoot = "keyFileRoot";
        public static String Experiment = "keyExperiment";
        public static String SessionID = "experimentIDKey";



    }
    public static class ActivityOnResult{

        public static final int OpenKey = 0x001;

    }


    public static class ResultCode{

        public static int FileOpenSuccessful = 0x000;
        public static int FileOpenUnSuccessful = 0x001;

    }

    public static class UpdateKeys {
        public static String State = "state";
        public static String WindSpeed = "windSpeed";
        public static String WindDirection = "windDir";
        public static String Pressure = "pressure";
        public static String Side = "side";
        public static String Lift = "lift";
        public static String Drag = "drag";
        public static String Humidity = "humidity";
        public static String Temperature = "temperature";
    }


}
