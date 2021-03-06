#include <QCoreApplication>
#include <QString>
#include <QCommandLineParser>
#include <QCommandLineOption>
#include <stdio.h>
#include <iostream>
#include <QFile>
#include <QList>
#include <QDataStream>
#include <QJsonDocument>
#include <QJsonObject>
#include <binary.h>
#include <export.h>

// loader version
#define APP_VERSION "1.0.0"



/**
 * @brief Application entry point
 * @param argc Number of arguments
 * @param argv  arguments values
 * @return 0 if no erreur encounted, -1 otherwise
 */

int main(int argc, char *argv[])
{

    QList<SensorRecord*> records;

    QCoreApplication app(argc, argv);
    QCoreApplication::setApplicationName("loader");
    QCoreApplication::setApplicationVersion(APP_VERSION);


    /* Arguments processing */
    QCommandLineParser parser;
    parser.addVersionOption();
    parser.applicationDescription();
    parser.addPositionalArgument("input","Path of binary file to parse");
    parser.addPositionalArgument("output","Path of json file to Write");
    parser.addOption({"f", "format", "output format (CSV or elastic for bulk elastic search format)"});
    parser.process(app);

    QStringList args = parser.positionalArguments();
    if(args.length() < 2)
    {
        qInfo("usage : loader <fileIn> <fileOut> [<-f [csv|json]>]");
        return 0;
    }

    QString inputFilePath = args.at(0);
    QString outputFilePath = args.at(1);
    QString format = parser.value("f");

    QFile rawFile(inputFilePath);
    if( !rawFile.exists())
    {
        qInfo("File '%s' not found.\n",  inputFilePath.toLatin1().data());
        return -1;
    }

    if(!rawFile.open(QIODevice::ReadOnly))
    {
        qInfo("Unable to open '%s'\n", inputFilePath.toLatin1().data());
        return -1;
    }

    QString cowType = readCowType(&rawFile);
    SensorRecord* record = nullptr;
    QDataStream ds(&rawFile);
    ds.setByteOrder(QDataStream::BigEndian);
    qInfo("Cow type : %s", cowType.toLatin1().data());
    while ( ( record = readRecord(&ds)) != nullptr ) {
        records.append(record);
    }
    rawFile.close();

    qInfo("%d loaded records", records.length());

    QFile jsOut(outputFilePath);
    QByteArray data = NULL;
    if(format == "json")
    {
        data    = bulkExportJson(cowType, records);
    }
    else
    {
        data    = bulkExportCsv(cowType, records);

    }

    jsOut.open(QIODevice::WriteOnly);
    jsOut.write(data);
    jsOut.flush();
    jsOut.close();

    return 0;
}
