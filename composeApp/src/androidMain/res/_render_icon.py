from PIL import Image, ImageDraw
from pathlib import Path

OUT = 512
SCALE = 4
SIZE = OUT * SCALE
S = SIZE / 108.0
cx = cy = 54 * S
# Smaller disc so the icon doesn't fill the whole square
disc_r_vp = 22  # was 28 / 36
k = disc_r_vp / 28

img = Image.new("RGBA", (SIZE, SIZE), (248, 250, 252, 255))

r = disc_r_vp * S
grad = Image.new("RGBA", (SIZE, SIZE), (0, 0, 0, 0))
x0, y0 = (54 - 28) * S, (54 - 28) * S
x1, y1 = (54 + 28) * S, (54 + 28) * S
dx, dy = x1 - x0, y1 - y0
den = dx * dx + dy * dy
y_lo, y_hi = int(cy - r) - 1, int(cy + r) + 2
x_lo, x_hi = int(cx - r) - 1, int(cx + r) + 2
for y in range(y_lo, y_hi):
    for x in range(x_lo, x_hi):
        if (x - cx) ** 2 + (y - cy) ** 2 > r * r:
            continue
        t = ((x - x0) * dx + (y - y0) * dy) / den
        t = 0 if t < 0 else 1 if t > 1 else t
        rr = int(0x00 + (0x3B - 0x00) * t)
        gg = int(0x50 + (0x71 - 0x50) * t)
        bb = int(0xD6 + (0xF7 - 0xD6) * t)
        grad.putpixel((x, y), (rr, gg, bb, 255))
img = Image.alpha_composite(img, grad)
draw = ImageDraw.Draw(img)


def sx(x: float) -> float:
    return cx + (x * S - cx) * k


def sy(y: float) -> float:
    return cy + (y * S - cy) * k


sw = max(2, int(round(3.2 * S * k)))
draw.line([(sx(48), sy(46)), (sx(48), sy(41))], fill=(255, 255, 255, 255), width=sw)
draw.line([(sx(60), sy(41)), (sx(60), sy(46))], fill=(255, 255, 255, 255), width=sw)
cr = sw / 2
for px, py in [(sx(48), sy(46)), (sx(48), sy(41)), (sx(60), sy(41)), (sx(60), sy(46))]:
    draw.ellipse([px - cr, py - cr, px + cr, py + cr], fill=(255, 255, 255, 255))
draw.arc([sx(48), sy(35), sx(60), sy(47)], start=180, end=0, fill=(255, 255, 255, 255), width=sw)
draw.rounded_rectangle(
    [sx(44), sy(46), sx(64), sy(66)],
    radius=3 * S * k,
    fill=(255, 255, 255, 255),
)

kh = Image.new("RGBA", (SIZE, SIZE), (0, 0, 0, 0))
khd = ImageDraw.Draw(kh)
kr = 2.5 * S * k
kcx, kcy = sx(54), sy(52)
khd.ellipse([kcx - kr, kcy - kr, kcx + kr, kcy + kr], fill=(0, 80, 214, 255))
stem_w = 1.5 * S * k
stem_top = sy(54.5)
stem_bot = sy(59.5)
khd.rectangle([kcx - stem_w, stem_top, kcx + stem_w, stem_bot], fill=(0, 80, 214, 255))
khd.ellipse(
    [kcx - stem_w, stem_bot - stem_w, kcx + stem_w, stem_bot + stem_w],
    fill=(0, 80, 214, 255),
)
img = Image.alpha_composite(img, kh)

img = img.resize((OUT, OUT), Image.Resampling.LANCZOS)
out = Path(__file__).with_name("passilius-icon.png")
img.save(out, "PNG", optimize=True)
print(f"wrote {out} {img.size} {out.stat().st_size} bytes")
