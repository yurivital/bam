#include <QJsonDocument>
#include <QJsonArray>
#include <QJsonObject>
#include <QJsonValue>
#include <export.h>
#include <QNetworkRequest>
#include <QVector2D>
static QString bulkCmd = "{\"index\": { \"_index\": \"bam\", \"_type\" : \"cow\"}}\n";

QByteArray  bulkExport(QString cowType, const QList<SensorRecord*> &list ){

    QByteArray result;

    foreach (SensorRecord* r, list) {

        result.append(bulkCmd);
        result.append( serialize(cowType, r));
        result.append("\n");
    }

    return result;
}

QByteArray serialize(QString cowType, SensorRecord* r){

    QJsonObject o;
    QJsonDocument doc;
    o.insert("timestamp", QJsonValue(r->timestamp/1000));
    o.insert("x", QJsonValue(r->x));
    o.insert("y", QJsonValue(r->y));
    o.insert("z", QJsonValue(r->z));
    o.insert("cow", QJsonValue(cowType));
/*
 * Calc the dot product

    QVector2D::dotProduct()
  */
    doc.setObject(o);
    return doc.toJson(QJsonDocument::Compact);
}
