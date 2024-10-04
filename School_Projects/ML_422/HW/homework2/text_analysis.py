def count_words_origin(phrase):
    '''Takes in a phrase and returns a dictionary of word counts'''
    words = phrase.split()
    result = {}
    for word in words:
        if word not in result:
            result[word] = 1  # Initialize with 1 for the first occurrence
        else:
            result[word] += 1  # Increase the count by 1 if word has already been stored
    return result

def clean_text(phrase):
    '''Takes in a phrase, removes punctuations, and makes all the text lowercase'''
    phrase = phrase.lower()
    punctuations = '.,?!\'"'  # Set puncatuations 
    for punctuation in punctuations:  # Remove each puncatuation one at a time
        phrase = phrase.replace(punctuation, '')  # Store the result back to phrase
    return phrase

def count_words(phrase):
    '''Takes in a phrase and returns a dictionary of word counts'''
    phrase = clean_text(phrase)
    words = phrase.split()
    result = {}
    for word in words:
        if word not in result:
            result[word] = 1  # Initialize with 1 for the first occurrence
        else:
            result[word] += 1  # Increase the count by 1 if word has already been stored
    return result

def most_common(phrase):
    '''Takes in a phrase and returns the most common word from the string'''
    counter = count_words(phrase)
    # Return the key with the highest count
    most_common_word = max(counter, key = counter.get)
    return most_common_word

def count_words_from_documents(documents):
    '''Cleans the text, counts the words across all the documents and returns the most common word'''
    full_document = ' '.join(documents)# Join all document together
    return most_common(full_document)

def count_words_from_file(file_name):
    '''Reads the file, cleans the text, counts the words and returns the most common word'''
    file = open(file_name, 'r', encoding = 'utf') 
    doc = file.read() # Read file
    file.close()
    counter = count_words(doc) # Because doc is not a list of string, we cannot use most_common directly
    return max(counter, key = counter.get)

def top_n_words_1(phrase, n = 3):
    '''Takes in a string and returns the top n words specified by the user'''
    counter = count_words(phrase)
    sorted_counter = sorted(counter, key = lambda x: counter[x], reverse = True) # Sort the counter by the values of each key
    result = ''# Create a variable to store the output string
    for i in range(n):
        result += (f'The word \'{sorted_counter[i]}\' appears {counter[sorted_counter[i]]} times.') # Add each key with their appearances
    return result

def top_n_words_2(input_data, n = 3):
    '''Takes in either a string, list of string, or file name, return the top n words specified by the user'''
    if isinstance(input_data, list):# First check if input_date is a list
        phrase = ' '.join(input_data)
    else:
        try:
            file = open(input_data, 'r', encoding='utf-8') # If we can open it, treat it as a file
            phrase = file.read()
            file.close()
        except FileNotFoundError:
            # If it can't be opened, treat it as a regular string
            phrase = input_data
    counter = count_words(phrase)
    sorted_counter = sorted(counter, key = lambda x: counter[x], reverse = True)
    result = ''
    for i in range(n):
        result += (f'The word \'{sorted_counter[i]}\' appears {counter[sorted_counter[i]]} times.')
    return result  

def top_n_words(input_data, n = 3):
    '''Takes in either a string, list of string, or file name, return the top n words specified by the user'''
    if isinstance(input_data, list):# First check if input_date is a list
        phrase = ' '.join(input_data)
    else:
        try:
            file = open(input_data, 'r', encoding='utf-8') # If we can open it, treat it as a file
            phrase = file.read()
            file.close()
        except FileNotFoundError:
            # If it can't be opened, treat it as a regular string
            phrase = input_data
    counter = count_words(phrase)
    sorted_counter = sorted(counter, key = lambda x: counter[x], reverse = True)
    i = 0
    prev_max = counter[sorted_counter[i]] # Store the previous highest occurence
    result = ''
    while n > 1:
        if i >= len(sorted_counter): # Check if the number of tied words is out of bound
            print('\nToo many ties, out of bound')
            break
        value = counter[sorted_counter[i]]
        # If the value is less than prev_max, meaning there is no tie, hence we could proceed to next highest occurence
        if counter[sorted_counter[i]] < prev_max: 
            n -= 1
            prev_max = value
        result += (f'The word \'{sorted_counter[i]}\' appears {counter[sorted_counter[i]]} times. ')
        i += 1 # Point to next word in the sorted_counter list
    return result

if __name__ == "__main__":
    test_phrase = (
        "I love pizza. Pizza is my favorite food. Everyone loves pizza, "
        "but I also enjoy pasta. Pasta with tomato sauce and cheese is delicious. "
        "On weekends, I like to make pizza or pasta for dinner. Pizza is always a hit!"
    )
    print(top_n_words(test_phrase, n=5))