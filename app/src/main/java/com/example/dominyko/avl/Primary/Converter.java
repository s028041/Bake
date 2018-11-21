package com.example.dominyko.avl.Primary;

import android.util.Log;

public class Converter {
    /**
     * <h1>Converts byte array to hex string</h1>
     * <p>Method gets bytes array and bytes lenght. Converts to string, the method is using String builder</p>
     * @param bytes this is the first parameter to convert bytes
     * @param lenght this is the second parameter to get lenght of received data size, from read input.
     * @return this returns converted String from byte array.
     */

    public String BytesArrayToHex(byte[] bytes, int lenght)
    {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes)
        {
          sb.append(String.format("%02x", b));
        }
        String Data = sb.toString();
        Data = Data.replace("", "");
        Data = Data.substring(0,lenght*2);
        return Data;
    }


    /**
     * <h1>Converts String to HEX byte array</h1>
     * <p>Method that converts string to HEX byte array</p>
     * @param text this is the first parameter to convert string to HEX bytes
     * @return this returns hex byte array from String
     */
    public byte[] StringToByteArray(String text)
    {

      byte[] b = new byte[text.length() / 2];
        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(text.substring(index, index + 2), 16);
            b[i] = (byte) v;
        }
        return b;

    }

    /**
     * <h1>Converts String to HEX byte array</h1>
     * <p>Method that converts string to byte array</p>
     * @param text this is the first parameter to convert string to bytes
     * @return this returns converted Byte array from String
     */

    public byte[] toHexBytes(String text)
    {
        int len = text.length();
        byte [] data = new byte[len / 2];
        for (int i = 0; i< len; i += 2)
        {
            data [i / 2] = (byte) ((Character.digit(text.charAt(i),16)<< 4)
            + Character.digit(text.charAt(i+1),16));
        }
        return data;
    }

    public String BytesArrayToHex(byte[] bytes)
    {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes)
        {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * <h1>Read and converts IMEI</h1>
     * <p> Method converts imei from string hex to string</p>
     * @param imei this is the first parameter to convert hex to string
     * @return this returns String imei of converted hex
     */

    public String ReadImei(String imei)
    {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < imei.length(); i = i+2)
        {
            String s = imei.substring(i,i + 2);               //Split the hex string into two character group
            int n = Integer.valueOf(s,16);                // Convert the each character group into integer using valueOf method
            builder.append((char)n);                        // Cast the integer value char
        }
        return builder.toString();
    }

    /**
     * <h1>Convert Long</h1>
     * <p>Method converts String to long hex</p>
     * @param hex method takes String value
     * @return returns hex long value
     */

    public long convertLong(String hex)
    {
        return Long.parseLong(hex,16);
    }

    /**
     * <h1>Convert INT</h1>
     * <p>Method converts String to hex</p>
     * @param hex method takes String value
     * @return returns hex int value
     */

    public int convertStringToIntHex(String hex)
    {
        try {
            return Integer.parseInt(hex,16);
        }catch (NumberFormatException e)
        {
            Log.d("Parse","Could not parse");
            return 0;
        }
    }

    public String hexToAscii(String hexStr)
    {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < hexStr.length(); i+=2)
        {
            String str = hexStr.substring(i,i+2);
            output.append((char) Integer.parseInt(str,16));
        }
        return output.toString();
    }


}
