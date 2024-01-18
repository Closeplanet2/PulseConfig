package com.pandapulsestudios.pulseconfig.Serializers;

import com.pandapulsestudios.pulseconfig.Interfaces.PulseClass;
import com.pandapulsestudios.pulseconfig.Interfaces.PulseConfig;
import com.pandapulsestudios.pulseconfig.Interfaces.SaveName;
import com.pandapulsestudios.pulseconfig.Objects.ConfigObject;
import com.pandapulsestudios.pulsecore.Data.API.VariableAPI;
import com.pandapulsestudios.pulsecore.Data.Interface.CustomVariable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ConfigSerializer {
    public static void SaveConfig(PulseConfig pulseConfig, ConfigObject configObject, boolean debugSave) throws Exception {
        var storedData = new HashMap<String, Object>();
        var dataFields = SerializerHelpers.ReturnAllFields(pulseConfig, null, null);
        for (var field : dataFields.keySet()){
            var fieldName = field.isAnnotationPresent(SaveName.class) ? field.getAnnotation(SaveName.class).value() : field.getName();
            storedData.put(fieldName, SaveConfig(dataFields.get(field)));
        }
        configObject.Set(pulseConfig.documentID(), storedData, debugSave);
    }

    private static Object SaveConfig(PulseClass pulseClass) throws Exception {
        var storedData = new HashMap<String, Object>();
        var dataFields = SerializerHelpers.ReturnAllFields(null, null, pulseClass);
        for (var field : dataFields.keySet()){
            var fieldName = field.isAnnotationPresent(SaveName.class) ? field.getAnnotation(SaveName.class).value() : field.getName();
            storedData.put(fieldName, SaveConfig(dataFields.get(field)));
        }
        return storedData;
    }

    private static HashMap<Object, Object> SaveConfig(HashMap<Object, Object> currentData) throws Exception {
        var returnData = new HashMap<>();
        for (var key : currentData.keySet()) returnData.put(SaveConfig(key), SaveConfig(currentData.get(key)));
        return returnData;
    }

    private static ArrayList<Object> SaveConfig(ArrayList<Object> currentData) throws Exception {
        var returnData = new ArrayList<>();
        for (var data : currentData) returnData.add(SaveConfig(data));
        return returnData;
    }

    private static Object SaveConfig(Object data) throws Exception {
        if(data instanceof PulseClass) return SaveConfig((PulseClass) data);
        else if(data instanceof HashMap) return SaveConfig((HashMap<Object, Object>) data);
        else if(data instanceof ArrayList) return SaveConfig((ArrayList<Object>) data);
        else if(data instanceof CustomVariable) return ((CustomVariable) data).SerializeData();
        else if(ConfigurationSerializable.class.isAssignableFrom(data.getClass())) return data;
        else if(data instanceof Date) return SerializerHelpers.SimpleDateFormat.format((Date) data);
        else{
            var variableTest = VariableAPI.RETURN_TEST_FROM_TYPE(data.getClass());
            return variableTest == null ? null : variableTest.SerializeData(data);
        }
    }


}