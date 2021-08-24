package com.mflyyou.cloud;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserDirDemo2 {
    public static void main(String[] args) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        System.out.println(LocalDateTime.now().format(dateTimeFormatter));
    }
}
