#include <QCoreApplication>
#include <QString>
#include <QCommandLineParser>
#include <QCommandLineOption>
#include <stdio.h>
#include <iostream>
#include <QFile>

#define APP_VERSION "1.0.0"


int main(int argc, char *argv[])
{

    QCoreApplication app(argc, argv);
    QCoreApplication::setApplicationName("loader");
    QCoreApplication::setApplicationVersion(APP_VERSION);


    /* Arguments processing */
    QCommandLineParser parser;
    parser.addVersionOption();
    parser.applicationDescription();
    parser.addPositionalArgument("input","Path of binary file to parse");

    parser.process(app);

    QStringList args = parser.positionalArguments();
    QString inputFilePath = args.at(0);

    QFile rawFile(inputFilePath);
   if( !rawFile.exists())
   {
       printf("File not found.");
       return -1;
   }

   if(!rawFile.open(QIODevice::ReadOnly))
   {
       printf("Unable to open file. %s", inputFilePath.toStdString());
       return -1;
   }



    return 0;
}
