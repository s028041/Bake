package com.example.dominyko.avl.Primary;

public class Crc16 {
    private int offset;
    private int polynom;
    private int preset;



    public Crc16(int offset, int polynom)
    {
        this.offset = offset;
        this.polynom = polynom;
    }

    public Crc16()
    {
        offset = 0;
        polynom = 0xA0001;
        preset = 0;
    }

    /**
     * <h>CRC16 calculates bytes array</h>
     * <p>Calculates crc16 of given byte array</p>
     * @param bytes this is the first parameter for calculating bytes
     * @return returns int value
     */
    public int getCRC(byte[] bytes) {return CalcCrc16(bytes);}

    private int CalcCrc16(byte[] buffer)
    {
        polynom &= 0xFFFF;

        int crc = preset;
        for (int i = 0; i< buffer.length; i++)
        {
            int data = buffer[(i+offset) % buffer.length] & 0xFF;
            crc ^= data;
            for (int j = 0; j<8; j++)
            {
                if((crc & 0x0001) !=0)
                {
                    crc = (crc >> 1) ^ polynom;
                }
                else
                {
                    crc = crc >> 1;
                }
            }
        }
        return crc & 0xFFFF;
    }
}
