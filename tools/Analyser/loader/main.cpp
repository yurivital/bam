#include <QCoreApplication>
#include <QString>
#include <QCommandLineParser>
#include <QCommandLineOption>
#include <stdio.h>
#include <iostream>
#include <QFile>
#include <QTextCodec>
#include <QList>
#include <QDataStream>

// loader version
#define APP_VERSION "1.0.0"


/**
  * Structure of one record
  */
struct SensorRecord  {
    long long timestamp;
    float x;
    float y;
    float z;
} ;


/**
 * @brief read cow type from file
 * @param file is an instance of QFile already opened
 * @return the cow race value
 */
QString readCowType(QFile* file){
    QTextCodec* codec = QTextCodec::codecForName("UTF-16BE");
    QString cowType ="";
    bool eol = false;
    while(!eol ){
        QByteArray buffer = file->read(2);
        QString ch  = codec->toUnicode(buffer);
        if(ch.data()[0] == 13){
            eol = true;
        }
        else
        {
            cowType.append(ch);
        }
    }

    return cowType;
}

/**
 * @brief swap byte for Big Endian to Little Endian conversion
 * @param buffer array
 * @param T target conversion type
 */
template<typename T> T swap(char* buffer){
    T valueT =0;
    char* value = (char*)&valueT;
    int s = sizeof(T);
    for(int i=0; i <s;i++){
        value[i] = buffer[s-(i+1)];
    }
    return valueT;
}



/**
 * @brief read an data record
 * @param stream instance of opened QDataStream
 * @return an pointer to readed sensorRecord
 */
SensorRecord* readRecord(QDataStream* stream)
{
    if(stream == nullptr){
        return nullptr;
    }

    SensorRecord*  r = nullptr;
    char  dataT[8];
    char  dataX [4];
    char  dataY [4];
    char  dataZ [4];

    if(stream->readRawData(dataT, 8) == 8
            && stream->readRawData(dataX,4) == 4
            && stream->readRawData(dataY,4) == 4
            && stream->readRawData(dataZ,4) == 4)
    {
        r = (SensorRecord*)  malloc(sizeof(SensorRecord));
        r->timestamp =swap<long long>(dataT);
        r->x = swap<float>(dataX);
        r->y = swap<float>(dataY);
        r->z = swap<float>(dataZ);
    }

    return r;
}

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
    parser.process(app);

    QStringList args = parser.positionalArguments();
    QString inputFilePath = args.at(0);

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

    return 0;
}
