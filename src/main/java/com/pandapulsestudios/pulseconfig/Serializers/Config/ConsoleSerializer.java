package com.pandapulsestudios.pulseconfig.Serializers.Config;

import com.pandapulsestudios.pulseconfig.Interfaces.PulseClass;
import com.pandapulsestudios.pulseconfig.Interfaces.Config.PulseConfig;
import com.pandapulsestudios.pulseconfig.Objects.Savable.SaveableArrayList;
import com.pandapulsestudios.pulseconfig.Objects.Savable.SaveableHashmap;
import com.pandapulsestudios.pulseconfig.Serializers.SerializerHelpers;
import com.pandapulsestudios.pulsecore.Data.API.VariableAPI;
import com.pandapulsestudios.pulsecore.Data.Interface.CustomVariable;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.Date;

public class ConsoleSerializer {
    public static String ConsoleOutput(PulseConfig pulseConfig) throws Exception {
        var stringBuilder = new StringBuilder(String.format("==========[%s]==========\n{\n", pulseConfig.documentID()));

        var dataFields = SerializerHelpers.ReturnALlFields(pulseConfig);
        for(var field : dataFields.keySet()){
            var fieldName = SerializerHelpers.ReturnFieldName(field);
            var fieldValue = ConsoleOutput(dataFields.get(field), 2);
            if(fieldValue == null) continue;
            stringBuilder.append(String.format("%s%s:%s\n", ReturnIndent(1), fieldName, fieldValue.toString()));
        }

        stringBuilder.append("}\n==========[END]==========");
        return stringBuilder.toString();
    }

    private static String ConsoleOutput(PulseClass pulseClass, int indent) throws Exception {
        var stringBuilder = new StringBuilder("<");

        var dataFields = SerializerHelpers.ReturnALlFields(pulseClass);
        for(var field : dataFields.keySet()){
            var fieldName = SerializerHelpers.ReturnFieldName(field);
            var fieldValue = ConsoleOutput(dataFields.get(field), 0);
            if(fieldValue == null) continue;
            stringBuilder.append(String.format("\n%s%s:%s", ReturnIndent(indent + 1), fieldName, fieldValue.toString()));
        }

        stringBuilder.append(String.format("\n%s>", ReturnIndent(indent - 1)));
        return stringBuilder.toString();
    }

    private static String ConsoleOutput(SaveableHashmap saveableHashmap, int indent) throws Exception {
        if(saveableHashmap.hashMap.isEmpty()) return "{}";

        var stringBuilder = new StringBuilder("{");
        for(var key : saveableHashmap.hashMap.keySet()){
            var skey = ConsoleOutput(key, indent);
            var sValue = ConsoleOutput(saveableHashmap.hashMap.get(key), indent);
            stringBuilder.append(String.format("\n%s%s:%s", ReturnIndent(indent), skey, sValue));
        }

        stringBuilder.append(String.format("\n%s}", ReturnIndent(indent - 1)));
        return stringBuilder.toString();
    }

    private static String ConsoleOutput( SaveableArrayList saveableArrayList, int indent) throws Exception {
        if(saveableArrayList.arrayList.isEmpty()) return "[]";

        var stringBuilder = new StringBuilder("[");
        for(var value : saveableArrayList.arrayList){
            var sValue = ConsoleOutput(value, indent);
            stringBuilder.append(String.format("\n%s%s", ReturnIndent(indent), sValue));
        }

        stringBuilder.append(String.format("\n%s]", ReturnIndent(indent - 1)));
        return stringBuilder.toString();
    }

    private static String ConsoleOutput(Object storedData, int indent) throws Exception{
        if(storedData == null) return null;
        if(storedData instanceof PulseClass pulseClass){
            return ConsoleOutput(pulseClass, indent + 1);
        }else if(storedData instanceof SaveableHashmap saveableHashmap){
            return ConsoleOutput(saveableHashmap, indent + 1);
        }else if(storedData instanceof SaveableArrayList saveableArrayList){
            return ConsoleOutput(saveableArrayList, indent + 1);
        }else if(storedData instanceof CustomVariable customVariable){
            return customVariable.SerializeData().toString();
        }else if(ConfigurationSerializable.class.isAssignableFrom(storedData.getClass())){
            return storedData.toString();
        }else if(storedData instanceof Date date){
            return date.toString();
        }else{
            var variableTest = VariableAPI.RETURN_TEST_FROM_TYPE(storedData.getClass());
            return variableTest == null ? null : String.format("%s%s", ReturnIndent(indent), variableTest.SerializeData(storedData).toString());
        }
    }

    private static String ReturnIndent(int indent){
        return " ".repeat(Math.max(0, indent));
    }
}
