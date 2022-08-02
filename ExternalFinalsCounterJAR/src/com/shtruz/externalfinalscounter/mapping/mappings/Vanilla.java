package com.shtruz.externalfinalscounter.mapping.mappings;

import com.shtruz.externalfinalscounter.mapping.Mapping;

public class Vanilla extends Mapping {
    @Override
    protected void setupNames() {
        names.get("auq").setLeft("auq");

        names.get("fa").setLeft("fa");

        names.get("auk").setLeft("auk");
        names.get("auk").getRight().replace("d()Ljava/lang/String;", "d");

        names.get("eu").setLeft("eu");
        names.get("eu").getRight().replace("c()Ljava/lang/String;", "c");
        names.get("eu").getRight().replace("d()Ljava/lang/String;", "d");

        names.get("auo").setLeft("auo");
        names.get("auo").getRight().replace("a(I)Lauk;", "a");
        names.get("auo").getRight().replace("i(Lauk;)Ljava/util/Collection;", "i");
        names.get("auo").getRight().replace("h(Ljava/lang/String;)Laul;", "h");

        names.get("bdc").setLeft("bdc");
        names.get("bdc").getRight().replace("a()Lcom/mojang/authlib/GameProfile;", "a");

        names.get("ave").setLeft("ave");
        names.get("ave").getMiddle().replace("f", "f");
        names.get("ave").getMiddle().replace("w", "w");
        names.get("ave").getMiddle().replace("t", "t");
        names.get("ave").getMiddle().replace("k", "k");
        names.get("ave").getMiddle().replace("h", "h");
        names.get("ave").getRight().replace("A()Lave;", "A");
        names.get("ave").getRight().replace("D()Lbde;", "D");
        names.get("ave").getRight().replace("av()V", "av");

        names.get("adm").setLeft("adm");
        names.get("adm").getRight().replace("Z()Lauo;", "Z");

        names.get("aum").setLeft("aum");
        names.get("aum").getRight().replace("e()Ljava/lang/String;", "e");

        names.get("aul").setLeft("aul");
        names.get("aul").getRight().replace("a(Lauq;Ljava/lang/String;)Ljava/lang/String;", "a");

        names.get("bde").setLeft("bde");
        names.get("bde").getMiddle().replace("b", "b");

        names.get("nx").setLeft("nx");
        names.get("nx").getRight().replace("a(Ljava/lang/String;)Ljava/lang/String;", "a");

        names.get("avh").setLeft("avh");
        names.get("avh").getMiddle().replace("aC", "aC");

        names.get("bfl").setLeft("bfl");
        names.get("bfl").getRight().replace("E()V", "E");
        names.get("bfl").getRight().replace("a(DDD)V", "a");
        names.get("bfl").getRight().replace("F()V", "F");

        names.get("avn").setLeft("avn");
        names.get("avn").getRight().replace("a(Ljava/lang/String;FFIZ)I", "a");

        names.get("wn").setLeft("wn");
        names.get("wn").getRight().replace("b(Leu;)V", "b");

        names.get("avt").setLeft("avt");
        names.get("avt").getRight().replace("a(Leu;)V", "a");

        names.get("bew").setLeft("bew");
        names.get("bew").getRight().replace("e(Ljava/lang/String;)V", "e");

        names.get("bfk").setLeft("bfk");
        names.get("bfk").getRight().replace("a(FJ)V", "a");

        names.get("awh").setLeft("awh");
        names.get("awh").getRight().replace("a(ILauo;Lauk;)V", "a");
        names.get("awh").getRight().replace("a(Lbdc;)Ljava/lang/String;", "a");
    }
}
