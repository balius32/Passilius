"""Check resource parity and common literal UI entry points; no third-party dependencies."""
from pathlib import Path
import re
import xml.etree.ElementTree as ET

ROOT = Path(__file__).resolve().parents[1]
BASE = ROOT / 'composeApp/src/commonMain/composeResources'

def read(folder):
    result = {}
    for element in ET.parse(BASE / folder / 'strings.xml').getroot():
        key = (element.tag, element.attrib['name'])
        assert key not in result, f'Duplicate {key}'
        texts = {child.attrib['quantity']: child.text for child in element} if element.tag == 'plurals' else {'value': element.text}
        assert all(text and text.strip() for text in texts.values()), f'Empty {key}'
        if element.tag == 'plurals': assert {'one','other'} <= texts.keys(), f'Missing plural forms: {key}'
        result[key] = texts
    return result

en, fa = read('values'), read('values-fa')
assert en.keys() == fa.keys(), 'Translation keys differ'
for key in en:
    for form in en[key]:
        assert sorted(re.findall(r'%\d+\$[sd]',en[key][form])) == sorted(re.findall(r'%\d+\$[sd]',fa[key][form])), f'Placeholder mismatch: {key}'

# Brand names, glyphs, password masks, numeric scale markers and test/animation IDs are not translated prose.
allowed = {'Passilius', 'N', '8', '20', '32'}
failures=[]
for source_set in ('commonMain','androidMain','desktopMain'):
    for path in (ROOT/'composeApp/src'/source_set/'kotlin').rglob('*.kt'):
        if not any(part in path.parts for part in ('presentation','components')): continue
        for line_number,line in enumerate(path.read_text(encoding='utf-8').splitlines(),1):
            for match in re.finditer(r'(?:text|contentDescription|hint|placeholderText)\s*=\s*"([^"\n]+)"',line):
                value=match.group(1)
                if value not in allowed and re.search('[A-Za-z]',value) and '$' not in value:
                    failures.append(f'{path.relative_to(ROOT)}:{line_number}: {value}')
assert not failures, '\n'.join(failures)
print(f'Validated {len(en)} bilingual resources and UI literal checks.')
