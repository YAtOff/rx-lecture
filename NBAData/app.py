import json
from collections import OrderedDict
import logging

import asyncio
from aiohttp import web


with open('data/nba.json') as f:
    teams = json.load(f)


async def team_list(request):
    await asyncio.sleep(0.5)
    data = [{
        'code': t['code'],
        'name': t['name'],
        'city': t['city']
    } for t in teams.values()]
    return web.json_response(data=data)


async def team_players(request):
    await asyncio.sleep(0.01)
    team = teams[request.match_info.get('code')]
    return web.json_response(data=list(team['players'].values()))


app = web.Application()
app.router.add_get('/teams', team_list)
app.router.add_get('/teams/{code}/players', team_players)
web.run_app(app, host='127.0.0.1', port=8080)
