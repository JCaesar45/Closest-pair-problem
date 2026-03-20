from dataclasses import dataclass
from typing import List, Tuple
import math

@dataclass
class Point:
    x: float
    y: float

def distance(p1: Point, p2: Point) -> float:
    return math.sqrt((p1.x - p2.x)**2 + (p1.y - p2.y)**2)

def brute_force_closest_pair(points: List[Point]) -> Tuple[float, List[Point]]:
    if len(points) < 2:
        return float('inf'), []
    
    min_dist = float('inf')
    pair = []
    
    for i in range(len(points)):
        for j in range(i + 1, len(points)):
            d = distance(points[i], points[j])
            if d < min_dist:
                min_dist = d
                pair = [points[i], points[j]]
    
    pair.sort(key=lambda p: p.x)
    return min_dist, pair

def strip_closest(strip: List[Point], d: float) -> Tuple[float, List[Point]]:
    min_dist = d
    best_pair = []
    
    for i in range(len(strip)):
        for j in range(i + 1, len(strip)):
            if strip[j].y - strip[i].y >= min_dist:
                break
            dist = distance(strip[i], strip[j])
            if dist < min_dist:
                min_dist = dist
                best_pair = [strip[i], strip[j]]
    
    if len(best_pair) == 2:
        best_pair.sort(key=lambda p: p.x)
    
    return min_dist, best_pair

def closest_pair_util(Px: List[Point], Py: List[Point]) -> Tuple[float, List[Point]]:
    n = len(Px)
    
    if n <= 3:
        return brute_force_closest_pair(Px)
    
    mid = n // 2
    mid_point = Px[mid]
    
    left_Px = Px[:mid]
    right_Px = Px[mid:]
    
    left_Py = [p for p in Py if p.x <= mid_point.x]
    right_Py = [p for p in Py if p.x > mid_point.x]
    
    dl = closest_pair_util(left_Px, left_Py)
    dr = closest_pair_util(right_Px, right_Py)
    
    dmin, pair_min = (dl[0], dl[1]) if dl[0] < dr[0] else (dr[0], dr[1])
    
    strip = [p for p in Py if abs(p.x - mid_point.x) < dmin]
    
    ds, pair_s = strip_closest(strip, dmin)
    
    if ds < dmin:
        return ds, pair_s
    
    return dmin, pair_min

def get_closest_pair(points: List[Point]) -> dict:
    if len(points) < 2:
        return {"distance": float('inf'), "pair": []}
    
    Px = sorted(points, key=lambda p: p.x)
    Py = sorted(points, key=lambda p: p.y)
    
    dist, pair = closest_pair_util(Px, Py)
    
    if len(pair) == 2:
        pair.sort(key=lambda p: p.x)
    
    return {"distance": dist, "pair": pair}
