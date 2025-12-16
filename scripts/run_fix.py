import traceback
from pathlib import Path
import re
import xlwt

try:
    ROOT = Path(__file__).resolve().parents[1]
    INPUT = ROOT / "alunos.xls"
    OUTPUT = ROOT / "alunos_fixed.xls"

    print(f"INPUT: {INPUT}")
    print(f"OUTPUT: {OUTPUT}")
    if not INPUT.exists():
        print("Arquivo de entrada não encontrado.")
        raise SystemExit(1)

    b = INPUT.read_bytes()
    print(f"Tamanho (bytes): {len(b)}")

    s = b.decode('latin1', errors='ignore')

    name_pattern = re.compile(r"[A-ZÁÉÍÓÚÃÕÇÂÊÔÀÈÙÝí]+(?:[ \-][A-ZÁÉÍÓÚÃÕÇÂÊÔÀÈÙÝí]+){1,5}")
    phone_pattern = re.compile(r"\b\d{7,13}\b")

    names = name_pattern.findall(s)
    phones = phone_pattern.findall(s)

    print(f"Raw names found: {len(names)}")
    print(f"Raw phones found: {len(phones)}")

    bad_tokens = set(["WORKBOOK","ROOT","ENTRY","Arial","MbP","thicp","Alunos"])
    filtered_names = [n.strip() for n in names if len(n.strip()) > 3 and n.strip() not in bad_tokens]
    if not filtered_names:
        alt_pattern = re.compile(r"[A-Z][a-z]{2,}(?: [A-Z][a-z]{2,})+")
        filtered_names = alt_pattern.findall(s)

    print(f"Filtered names: {len(filtered_names)}")
    print(filtered_names[:10])
    print(phones[:10])

    rows = []
    max_pairs = max(len(filtered_names), len(phones))
    for i in range(max_pairs):
        nome = filtered_names[i] if i < len(filtered_names) else ''
        telefone = phones[i] if i < len(phones) else ''
        rows.append((i+1, False, nome, telefone))

    if not rows:
        rows = [(1, False, 'Nome Exemplo', '11900000000')]

    book = xlwt.Workbook(encoding='utf-8')
    sheet = book.add_sheet('Alunos')

    headers = ['id','inativo','nome','telefone']
    for c,h in enumerate(headers):
        sheet.write(0, c, h)

    for r, data in enumerate(rows, start=1):
        sheet.write(r, 0, data[0])
        sheet.write(r, 1, 'sim' if data[1] else 'nao')
        sheet.write(r, 2, data[2])
        sheet.write(r, 3, data[3])

    book.save(str(OUTPUT))
    print(f"Arquivo gerado: {OUTPUT} (linhas: {len(rows)})")

except Exception as e:
    print("Erro durante execução:")
    traceback.print_exc()
    raise

