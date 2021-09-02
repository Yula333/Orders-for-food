package com.itproger.orders.Helpers;

import android.content.Context;

import com.google.gson.Gson;
import com.itproger.orders.Models.Cart;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class JSONHelper {

    private static final String FINE_NAME = "shopping_cart.json";

    public static String createJSONString(List<Cart> dataList){
        Gson gson = new Gson();
        DataItems dataItems = new DataItems();
        dataItems.setCartList(dataList);
        return gson.toJson(dataItems);     //создаем json строку и возвращаем ее
    }

    public static boolean exportToJSON(Context context, List<Cart> dataList) {

        String jsonString = createJSONString(dataList);   //библиотека gson на основе объекта dataItems сформирует строку в
                                                        // формате json (типа {1:2, 6:1}  позиция: количество) для записи в файл
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = context.openFileOutput(FINE_NAME, Context.MODE_PRIVATE);
            fileOutputStream.write(jsonString.getBytes());
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fileOutputStream !=null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return  false;
    }

    public static List<Cart> importFromJSON(Context context) {  //метод для получения значения из файла и этот .json преобразовывать в список List<Cart>
        InputStreamReader streamReader = null;          //получаем данные из файла
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = context.openFileInput(FINE_NAME);
            streamReader = new InputStreamReader(fileInputStream);      //теперь можем считывать данные

            Gson gson = new Gson();
            DataItems dataItems = gson.fromJson(streamReader, DataItems.class);     //вытягиваем данные из streamReader
            return dataItems.getCartList();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally{
            if(streamReader != null) {
                try {
                    streamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private static class DataItems{

        private List<Cart> cartList;

        private List<Cart> getCartList(){
            return this.cartList;
        }

        public void setCartList(List<Cart> cartList){
            this.cartList = cartList;
        }
    }

}
