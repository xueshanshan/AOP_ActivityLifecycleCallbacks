package com.star.plugin

import com.android.build.gradle.AppPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile
import org.aspectj.bridge.IMessage
import org.aspectj.bridge.MessageHandler
import org.aspectj.tools.ajc.Main

class AopPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        project.extensions.create("aop", AopExtension)

        def app = project.plugins.withType(AppPlugin)
        def variants
        if (app) {
            variants = project.android.applicationVariants
        } else {
            variants = project.android.libraryVariants
        }

        //为app或module添加依赖
        project.dependencies {
            implementation 'org.aspectj:aspectjrt:1.8.10'
        }

        variants.all { variant ->
            if (!project.aop.enable) {
                return;
            }
            def enableLog = project.aop.enableLog
            JavaCompile javaCompile = variant.javaCompile
            javaCompile.doLast {
                String[] args = ["-showWeaveInfo",
                                 "-1.8",
                                 "-inpath", javaCompile.destinationDir.toString(),
                                 "-aspectpath", javaCompile.classpath.asPath,
                                 "-d", javaCompile.destinationDir.toString(),
                                 "-classpath", javaCompile.classpath.asPath,
                                 "-bootclasspath", project.android.bootClasspath.join(File.pathSeparator)]
                if (enableLog) {
                    logger.debug "ajc args: " + Arrays.toString(args)

                    println()
                    println("####################################################################")
                    println("########                                                    ########")
                    println("########                                                    ########")
                    println("########           欢迎使用 aop-plugin® 编译插件            ########")
                    println("########           使用过程中碰到任何问题请联系我           ########")
                    println("########           邮箱：xueshanshan_star@163.com           ########")
                    println("########                                                    ########")
                    println("####################################################################")
                    println()

                }

                MessageHandler handler = new MessageHandler(true)
                new Main().run(args, handler)
                if (enableLog) {
                    for (IMessage message : handler.getMessages(null, true)) {
                        switch (message.getKind()) {
                            case IMessage.ABORT:
                            case IMessage.ERROR:
                            case IMessage.FAIL:
                                logger.error message.message, message.thrown
                                break
                            case IMessage.WARNING:
                                logger.warn message.message, message.thrown
                                break
                            case IMessage.INFO:
                                logger.info message.message, message.thrown
                                break
                            case IMessage.DEBUG:
                                logger.debug message.message, message.thrown
                                break
                        }
                    }
                }
            }
        }
    }
}