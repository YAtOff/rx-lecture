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


async def team_detail(request):
    await asyncio.sleep(0.1)
    team = teams[request.match_info.get('code')]
    data = {
        'code': team['code'],
        'name': team['name'],
        'city': team['city'],
        'players': [player_code for player_code in team['players'].keys()]
    }
    return web.json_response(data=data)


async def player_detail(request):
    await asyncio.sleep(0.05)
    team = teams[request.match_info.get('team_code')]
    player = team['players'][request.match_info.get('player_code')]
    return web.json_response(data=player)


app = web.Application()
app.router.add_get('/teams', team_list)
app.router.add_get('/teams/{code}', team_detail)
app.router.add_get('/teams/{team_code}/players/{player_code}', player_detail)
web.run_app(app, host='127.0.0.1', port=8080)
