#ifndef BINARY_H
#define BINARY_H

#include <QString>
#include <QFile>
#include <QDataStream>
#include <QTextCodec>


/**
  * Structure of one record
  */
struct SensorRecord  {
    long long timestamp;
    float x;
    float y;
    float z;
} ;


QString readCowType(QFile* file);

SensorRecord* readRecord(QDataStream* stream);

template<typename T> T swap(char* buffer);

#endif // BINARY_H
