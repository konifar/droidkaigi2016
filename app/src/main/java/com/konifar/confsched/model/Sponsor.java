package com.konifar.confsched.model;

import java.util.ArrayList;
import java.util.List;

public class Sponsor {

    public final String imageUrl;

    public final String url;

    public Sponsor(String imageUrl, String url) {
        this.imageUrl = imageUrl;
        this.url = url;
    }

    public static List<Sponsor> createPlatinumList() {
        List<Sponsor> list = new ArrayList<>();
        list.add(new Sponsor("https://droidkaigi.github.io/2016/images/logo/mixi.png", "https://mixi.co.jp"));
        list.add(new Sponsor("https://droidkaigi.github.io/2016/images/logo/findjob.png", "http://www.find-job.net"));
        list.add(new Sponsor("https://droidkaigi.github.io/2016/images/logo/mixiagent.png", "http://mixi-agent.jp"));
        return list;
    }

    public static List<Sponsor> createVideoList() {
        List<Sponsor> list = new ArrayList<>();
        list.add(new Sponsor("https://droidkaigi.github.io/2016/images/logo/smartnews.png", "http://about.smartnews.com"));
        return list;
    }

    public static List<Sponsor> createFoodsList() {
        List<Sponsor> list = new ArrayList<>();
        list.add(new Sponsor("https://droidkaigi.github.io/2016/images/logo/sansan.png", "https://www.sansan.com"));
        list.add(new Sponsor("https://droidkaigi.github.io/2016/images/logo/mercari.png", "https://www.mercari.com/jp"));
        return list;
    }

    public static List<Sponsor> createNormalList() {
        List<Sponsor> list = new ArrayList<>();
        list.add(new Sponsor("https://droidkaigi.github.io/2016/images/logo/gmo_pepabo.png", "http://pepabo.com"));
        list.add(new Sponsor("https://droidkaigi.github.io/2016/images/logo/seesaa.png", "http://www.seesaa.co.jp"));
        list.add(new Sponsor("https://droidkaigi.github.io/2016/images/logo/cookpad.png", "https://info.cookpad.com"));
        list.add(new Sponsor("https://droidkaigi.github.io/2016/images/logo/zaim.png", "http://zaim.net"));
        list.add(new Sponsor("https://droidkaigi.github.io/2016/images/logo/deploygate.png", "https://deploygate.com"));
        list.add(new Sponsor("https://droidkaigi.github.io/2016/images/logo/fril.png", "https://fablic.co.jp"));
        list.add(new Sponsor("https://droidkaigi.github.io/2016/images/logo/caraquri.png", "http://caraquri.com"));
        list.add(new Sponsor("https://droidkaigi.github.io/2016/images/logo/goodpatch.png", "http://goodpatch.com"));
        list.add(new Sponsor("https://droidkaigi.github.io/2016/images/logo/uphyca.png", "http://www.uphyca.com"));
        list.add(new Sponsor("https://droidkaigi.github.io/2016/images/logo/gamewith.png", "http://gamewith.co.jp"));
        return list;
    }

}
