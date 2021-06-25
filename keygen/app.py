import json

import sympy
from flask import Flask, request, jsonify, Response
from flask_restful import Resource, Api
from model import Parameters, ParametersResponse, PublicKeyRequest
from persistence import DatabaseService

app = Flask(__name__)
api = Api(app)

BITS_SECURITY = 16

all_public_keys = []


class PublicKeyResource(Resource):

    def __init__(self):
        self.database_service = DatabaseService()

    def get(self):
        return jsonify(self.database_service.all_public_keys())

    def post(self):
        json_data = request.get_json(force=True)
        print(json_data)
        public_key_dto: PublicKeyRequest = PublicKeyRequest(values=json_data['values'], owner=json_data['owner'])
        public_key = public_key_dto.to_public_key()
        self.database_service.add_public_key(public_key)
        return jsonify(public_key.__dict__)


api.add_resource(PublicKeyResource, '/public-keys')

p = sympy.randprime(0, 2 ** BITS_SECURITY)
q = sympy.randprime(0, 2 ** BITS_SECURITY)
parameters: Parameters = Parameters(p, q)


class GenerateParameters(Resource):

    def __init__(self):
        pass

    def get(self):
        return jsonify(ParametersResponse.from_parameters(parameters).__dict__)


api.add_resource(GenerateParameters, '/parameters')

if __name__ == '__main__':
    app.run()
