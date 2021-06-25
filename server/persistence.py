import string

from model import Visit, VisitStatus

visits = {}


class DatabaseService(object):

    @staticmethod
    def add_visit(visit: Visit):
        if visit.visitId not in visits.keys():
            print('Adding new record')
            visits[visit.visitId] = {
                'visitId': visit.visitId,
                'authorId': visit.authorId,
                'author': visit.author,
                'date': visit.date,
                'status': visit.status,
                'guestId': visit.guestId,
                'protocol': visit.protocol
            }
            print(visits)

    @staticmethod
    def update_visit(visit_id, status, protocol, guest_id=None):
        visits[visit_id]['status'] = status
        visits[visit_id]['protocol'] = protocol
        if guest_id:
            visits[visit_id]['guestId'] = guest_id
        return visits[visit_id]

    @staticmethod
    def all_visits():
        return visits.values()

    @staticmethod
    def all_visits_by_status(status: VisitStatus):
        return [visit for visit in visits.values() if visit['status'] == status.name]

    @staticmethod
    def all_visits_by_status_and_guest_id(status: VisitStatus, guest_id: string):
        return [visit for visit in visits.values() if visit['guestId'] == guest_id and visit['status'] == status.name]

    @staticmethod
    def all_visits_by_status_and_author_id(status: VisitStatus, author_id: string):
        return [visit for visit in visits.values() if visit['authorId'] == author_id and visit['status'] == status.name]
