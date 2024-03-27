启动前，需要在 Nacos 控制台创建 `nacos-config-demo-dev.yaml` ，并添加 `config-test.str=` 的配置。 或者在 `bootstrap.yml` 中添加该配置。

否则初次载入不会读取配置，直接报错 NPE