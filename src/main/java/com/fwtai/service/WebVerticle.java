package com.fwtai.service;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;

/**
 * 数据库访问
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2021/2/11 1:51
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
*/
public final class WebVerticle extends AbstractVerticle {

  @Override
  public void start(final Promise<Void> start) throws Exception {
    doDatabaseMigrations().setHandler(start::handle);
}

  protected Future<Void> doDatabaseMigrations(){
    final JsonObject jsonObject = config().getJsonObject("db");
    final String url = jsonObject.getString("url","jdbc:mysql://192.168.3.66:3306/vertx04-final?useUnicode=true&characterEncoding=utf-8&useSSL=false&allowMultiQueries=true&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true");
    final String user = jsonObject.getString("userName","root");
    final String password = jsonObject.getString("password","rootFwtai");
    final Flyway flyway = Flyway.configure().dataSource(url,user,password).load();
    final Promise<Void> promise = Promise.promise();
    try {
      flyway.migrate();
      promise.complete();
    } catch (final FlywayException fe) {
      promise.fail(fe);
    }
    return promise.future();
  }
}