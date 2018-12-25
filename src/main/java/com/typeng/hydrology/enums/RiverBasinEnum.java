package com.typeng.hydrology.enums;

/**
 * 流域枚举.
 *
 * @author Tianying Peng
 * @date 2018-12-25 21:39
 */
public enum RiverBasinEnum {

    XIANG_JIANG(2, "湘江"),
    ZI_SHUI(3, "资水"),
    YUAN_SHUI(4, "沅水"),
    LI_SHUI(5, "澧水"),
    CHANG_JIANG(6, "长江"),
    DONG_TING_HU(7, "洞庭湖");

    private int key;
    private String name;

    RiverBasinEnum(int key, String name) {
        this.key = key;
        this.name = name;
    }

    public int getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    /**
     * 通过键获取枚举类.
     *
     * @param key
     * @return
     */
    public static RiverBasinEnum getEnumByKey(Integer key) {
        RiverBasinEnum[] riverBasinEnums = RiverBasinEnum.values();
        for (RiverBasinEnum riverBasinEnum : riverBasinEnums) {
            if (riverBasinEnum.getKey() == key) {
                return riverBasinEnum;
            }
        }
        return null;
    }

    /**
     * 通过流域名获取枚举类.
     *
     * @param name
     * @return
     */
    public static RiverBasinEnum getEnumByName(String name) {
        RiverBasinEnum[] riverBasinEnums = RiverBasinEnum.values();
        for (RiverBasinEnum riverBasinEnum : riverBasinEnums) {
            if (riverBasinEnum.getName().equals(name)) {
                return riverBasinEnum;
            }
        }
        return null;
    }

}


