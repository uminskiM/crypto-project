from flask import Flask, request, jsonify, Response
from flask_restful import Resource, Api

from model import CreateVisitRequest, VisitStatus, VisitResponse
from persistence import DatabaseService

app = Flask(__name__)
api = Api(app)

BITS_SECURITY = 5


class VisitResource(Resource):

    def __init__(self):
        self.database_service = DatabaseService()

    def post(self):
        json_data = request.get_json(force=True)
        print(json_data)
        create_visit_request: CreateVisitRequest = CreateVisitRequest(
            authorId=json_data['authorId'],
            author=json_data['author'],
            date=json_data['date'])
        visit = create_visit_request.to_visit()
        self.database_service.add_visit(visit)
        return Response('', status=201, mimetype='application/json')

    def get(self):
        status = request.args.get('status', None)
        if status is not None:
            visits = self.database_service.all_visits_by_status(VisitStatus[status])
        else:
            visits = self.database_service.all_visits()
        visits_response = [VisitResponse(visit).__dict__ for visit in visits]
        print(jsonify(visits_response))
        return jsonify(visits_response)

    def put(self):
        status = request.args.get('status', None)
        if status is None:
            return Response('', status=400, mimetype='application/json')
        visit_id = request.args.get('visit_id', None)
        if visit_id is None:
            return Response('', status=400, mimetype='application/json')
        protocol_json_data = request.get_json(force=True)
        protocol = {
            'x': protocol_json_data['x'],
            'verifierBits': protocol_json_data['verifierBits'],
            'y': protocol_json_data['y'],
            'holds': protocol_json_data['holds']
        }
        guest_id = request.args.get('guest_id', None)
        updated_visit = self.database_service.update_visit(guest_id=guest_id, visit_id=visit_id, status=status, protocol=protocol)
        return jsonify(updated_visit)


api.add_resource(VisitResource, '/visits')

if __name__ == '__main__':
    app.run()
