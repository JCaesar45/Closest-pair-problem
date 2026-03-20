class Point {
  constructor(x, y) {
    this.x = x;
    this.y = y;
  }
}

function distance(p1, p2) {
  const dx = p1.x - p2.x;
  const dy = p1.y - p2.y;
  return Math.sqrt(dx * dx + dy * dy);
}

function bruteForceClosestPair(points) {
  if (points.length < 2) return { distance: Infinity, pair: [] };
  
  let minDist = Infinity;
  let pair = [];
  
  for (let i = 0; i < points.length - 1; i++) {
    for (let j = i + 1; j < points.length; j++) {
      const d = distance(points[i], points[j]);
      if (d < minDist) {
        minDist = d;
        pair = [points[i], points[j]];
      }
    }
  }
  
  // Sort pair by x-coordinate
  pair.sort((a, b) => a.x - b.x);
  return { distance: minDist, pair };
}

function stripClosest(strip, d) {
  let minDist = d;
  let bestPair = [];
  
  for (let i = 0; i < strip.length; i++) {
    for (let j = i + 1; j < strip.length && (strip[j].y - strip[i].y) < minDist; j++) {
      const dist = distance(strip[i], strip[j]);
      if (dist < minDist) {
        minDist = dist;
        bestPair = [strip[i], strip[j]];
      }
    }
  }
  
  if (bestPair.length === 2) {
    bestPair.sort((a, b) => a.x - b.x);
  }
  
  return { distance: minDist, pair: bestPair };
}

function closestPairUtil(Px, Py) {
  const n = Px.length;
  
  if (n <= 3) {
    return bruteForceClosestPair(Px);
  }
  
  const mid = Math.floor(n / 2);
  const midPoint = Px[mid];
  
  const leftPx = Px.slice(0, mid);
  const rightPx = Px.slice(mid);
  
  // Create y-sorted arrays for left and right
  const leftPy = [];
  const rightPy = [];
  for (const p of Py) {
    if (p.x <= midPoint.x) {
      leftPy.push(p);
    } else {
      rightPy.push(p);
    }
  }
  
  const dl = closestPairUtil(leftPx, leftPy);
  const dr = closestPairUtil(rightPx, rightPy);
  
  let dmin = dl.distance < dr.distance ? dl : dr;
  
  // Build strip
  const strip = [];
  for (const p of Py) {
    if (Math.abs(p.x - midPoint.x) < dmin.distance) {
      strip.push(p);
    }
  }
  
  const stripResult = stripClosest(strip, dmin.distance);
  
  if (stripResult.distance < dmin.distance) {
    return stripResult;
  }
  
  return dmin;
}

function getClosestPair(points) {
  if (points.length < 2) {
    return { distance: Infinity, pair: [] };
  }
  
  // Sort by x and by y
  const Px = [...points].sort((a, b) => a.x - b.x);
  const Py = [...points].sort((a, b) => a.y - b.y);
  
  const result = closestPairUtil(Px, Py);
  
  // Make sure pair is always sorted by x
  if (result.pair.length === 2) {
    result.pair.sort((a, b) => a.x - b.x);
  }
  
  return result;
}
