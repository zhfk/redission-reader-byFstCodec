package com.zhfk.redissionreader.constant;

public enum Codecs {

    JSONJACKSONCODEC("org.redisson.codec.JsonJacksonCodec", "Jackson JSON 编码 默认编码"),
    AVROJACKSONCODEC("org.redisson.codec.AvroJacksonCodec", "Avro 一个二进制的JSON编码"),
    SMILEJACKSONCODEC("org.redisson.codec.SmileJacksonCodec", "Smile 另一个二进制的JSON编码"),
    CBORJACKSONCODEC("org.redisson.codec.CborJacksonCodec", "CBOR又一个二进制的JSON编码"),
    MSGPACKJACKSONCODEC("org.redisson.codec.MsgPackJacksonCodec", "MsgPack 再来一个二进制的JSON编码"),
    IONJACKSONCODEC("org.redisson.codec.IonJacksonCodec", "Amazon Ion 亚马逊的Ion编码，格式与JSON类似"),
    KRYOCODEC("org.redisson.codec.KryoCodec", "Kryo 二进制对象序列化编码"),
    SERIALIZATIONCODEC("org.redisson.codec.SerializationCodec", "JDK序列化编码"),
    FSTCODEC("org.redisson.codec.FstCodec", "FST 10倍于JDK序列化性能而且100%兼容的编码"),
    LZ4CODEC("org.redisson.codec.LZ4Codec", "LZ4 压缩型序列化对象编码"),
    SNAPPYCODEC("org.redisson.codec.SnappyCodec", "Snappy 另一个压缩型序列化对象编码"),
    JSONJACKSONMAPCODEC("org.redisson.client.codec.JsonJacksonMapCodec", "基于Jackson的映射类使用的编码。可用于避免序列化类的信息，以及用于解决使用byte[]遇到的问题"),
    STRINGCODEC("org.redisson.client.codec.StringCodec", "纯字符串编码（无转换）"),
    LONGCODEC("org.redisson.client.codec.LongCodec", "纯整长型数字编码（无转换）"),
    BYTEARRAYCODEC("org.redisson.client.codec.ByteArrayCodec", "字节数组编码"),
    COMPOSITECODEC("org.redisson.codec.CompositeCodec", "用来组合多种不同编码在一起");

    public String name;

    public String desc;

    Codecs(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }
}
