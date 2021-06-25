import string
import uuid
from enum import Enum


class VisitStatus(Enum):
    FREE = 1
    DECLARED = 2
    CHALLENGED = 3
    CONFIRMED = 4
    VERIFIED = 5
    REJECTED = 6


class CreateVisitRequest(object):

    def __init__(self, authorId: string, author: string, date: string):
        self.authorId = authorId
        self.author = author
        self.date = date

    def to_visit(self):
        return Visit(self.authorId, self.author, self.date)


class Visit(object):

    def __init__(self, authorId, author, date):
        self.visitId = str(uuid.uuid4())
        self.authorId = authorId
        self.author = author
        self.date = date
        self.status = VisitStatus.FREE.name
        self.guestId = None
        self.protocol = {}

    def update_protocol(self, protocol: dict):
        self.protocol = {
            'x': protocol['x'],
            'verifierBits': protocol['verifierBits'],
            'y': protocol['y'],
            'holds': protocol['holds']
        }

    def update_status(self, visit_status: VisitStatus):
        self.status = visit_status

    def enroll_user(self, guest_id: string):
        self.guestId = guest_id


class VisitResponse(object):

    def __init__(self, visit_as_dict):
        self.visitId = visit_as_dict['visitId']
        self.authorId = visit_as_dict['authorId']
        self.author = visit_as_dict['author']
        self.date = visit_as_dict['date']
        self.status = visit_as_dict['status']
        self.guestId = visit_as_dict['guestId']
        self.protocol = visit_as_dict['protocol']
