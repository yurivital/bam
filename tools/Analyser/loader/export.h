#ifndef EXPORT_H
#define EXPORT_H
#include <binary.h>
#include <QList>
#include <QString>

QByteArray serialize(QString cowType, SensorRecord* r);

QByteArray  bulkExportJson(QString cowType, const QList<SensorRecord*> &list);
QByteArray  bulkExportCsv(QString cowType, const QList<SensorRecord*> &list);

#endif // EXPORT_H
